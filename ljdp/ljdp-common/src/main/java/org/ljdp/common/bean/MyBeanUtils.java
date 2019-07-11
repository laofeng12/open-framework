package org.ljdp.common.bean;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ClassUtils;
import org.ljdp.util.DateFormater;

public class MyBeanUtils {
	/**
	 * 复制orig字段值到dest，不复制serialVersionUID字段
	 * @param dest
	 * @param orig
	 */
	public static void copyProperties(Object dest, Object orig) {
//		Map map = BeanUtils.describe(orig);
//		Iterator it = map.keySet().iterator();
//		while(it.hasNext()) {
//			String key = (String)it.next();
//			PropertyUtils.setSimpleProperty(dest, key, map.get(key));
//		}
		Field[] fields = orig.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; ++i) {
			String key = fields[i].getName();
			if(key.equals("serialVersionUID")) {
				continue;
			}
			try {
				PropertyUtils.setSimpleProperty(dest, key, PropertyUtils.getSimpleProperty(orig, key));				
			} catch(NoSuchMethodException e) {
				
			} catch(Exception e) {
				System.out.println("ERROR on COPY:"+key);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 只复制dest为空的字段。
	 * @param dest
	 * @param orig
	 */
	public static void copyPropertiesOfNull(Object dest, Object orig) {
		Field[] fields = orig.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; ++i) {
			String key = fields[i].getName();
			if(key.equals("serialVersionUID")) {
				continue;
			}
			try {
				if(PropertyUtils.getSimpleProperty(dest, key) == null) {					
					PropertyUtils.setSimpleProperty(
							dest, key, PropertyUtils.getSimpleProperty(orig, key));				
				}
			} catch(NoSuchMethodException e) {
				
			} catch(Exception e) {
				System.out.println("ERROR on COPY:"+key);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 只复制orig中不为空的字段。（把orig中不为空的字段copy到dest）
	 * @param dest
	 * @param orig
	 */
	public static void copyPropertiesNotNull(Object dest, Object orig) {
		Field[] fields = orig.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; ++i) {
			String key = fields[i].getName();
			if(key.equals("serialVersionUID")) {
				continue;
			}
			try {
				Object origVal = PropertyUtils.getSimpleProperty(orig, key);
				if(origVal != null) {
					PropertyUtils.setSimpleProperty(dest, key, origVal);				
				}
			} catch(NoSuchMethodException e) {
				
			} catch(Exception e) {
				System.out.println("ERROR on COPY:"+key);
				e.printStackTrace();
			}
		}
	}
	
	public static void copyPropertiesNotBlank(Object dest, Object orig) {
		Field[] fields = orig.getClass().getDeclaredFields();
		for(int i = 0; i < fields.length; ++i) {
			String key = fields[i].getName();
			if(key.equals("serialVersionUID")) {
				continue;
			}
			try {
				Object origVal = PropertyUtils.getSimpleProperty(orig, key);
				if(origVal != null) {
					if(origVal instanceof String) {
						if(StringUtils.isNotBlank(origVal.toString())) {
							PropertyUtils.setSimpleProperty(dest, key, origVal);
						}
					} else {
						PropertyUtils.setSimpleProperty(dest, key, origVal);
					}
				}
			} catch(NoSuchMethodException e) {
				
			} catch(Exception e) {
				System.out.println("ERROR on COPY:"+key);
				e.printStackTrace();
			}
		}
	}
	
	public static String getElementPropertyValue(Object element, int i,
			DynamicField field) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Object value = null;
		if (element.getClass().isArray()) {
			value = Array.get(element, i);
		} else if (element instanceof List) {
			value = ((List<?>) element).get(i);
		} else {
			int lastPoint = field.getName().lastIndexOf(".");
			if(lastPoint > 0) {
				String path = field.getName().substring(0, lastPoint);
				String name = field.getName().substring(lastPoint+1, field.getName().length());
				Object pathObject = PropertyUtils.getProperty(element, path);
				if(pathObject == null) {
					value = "";
				} else if(pathObject instanceof Map) {
					Map<?,?> map = (Map<?,?>)pathObject;
					if(map != null && map.containsKey(name)) {
						value = map.get(name);
					} else {
						value = "";
					}
				}
			}
			if(value == null) {
				if(StringUtils.isNotEmpty(field.getName())) {
					value = PropertyUtils.getProperty(element, field.getName());
				}
			}
		}
		if(value == null) {
			value = "";
		}
		String valStr =value.toString();
		if(field.getTransformer() != null) {
			Object tfVal = field.getTransformer().transform(value);
			if(tfVal != null) {
				valStr = tfVal.toString();
			}
		} else if(StringUtils.isNotBlank(field.getBaseType())) {
			if(field.getBaseType().equals(FieldType.BASE_DATE) 
					|| field.getBaseType().equals(FieldType.BASE_TIMESAMP)) {
				valStr = DateFormater.format(value, field.getCustomizeType());
			} else if(field.getBaseType().equals(FieldType.BASE_FLOAT)) {
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMaximumFractionDigits(2);
				nf.setGroupingUsed(false);
				valStr = nf.format(value);
			} else if(field.getBaseType().equals(FieldType.BASE_INTEGER)) {
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setGroupingUsed(false);
				valStr = nf.format(value);
			}
		}
		return valStr;
	}

	/**
	 * null值不输出
	 */
	public static StringBuffer reflectionToString(final Object obj) {
		StringBuffer sb = new StringBuffer(100);
		final Class<?> clazz = obj.getClass();
		sb.append(clazz.getSimpleName()).append("[");
		final Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		int i = 0;
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (accept(field)) {
                try {
                    final Object fieldValue = field.get(obj);
                    if(fieldValue != null) {
                    	if(i > 0) {
                    		sb.append(",");
                    	}
                    	String sval;
                    	if(fieldValue instanceof Date) {
                    		sval = DateFormater.formatDatetime((Date)fieldValue);
                    	} else {
                    		sval = fieldValue.toString();
                    	}
                    	sb.append(fieldName).append("=").append(sval);
                    	i++;
                    }
                } catch (final IllegalAccessException ex) {
                    //this can't happen. Would get a Security exception
                    // instead
                    //throw a runtime exception in case the impossible
                    // happens.
                    throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                }
            }
        }
        sb.append("]");
        return sb;
	}
	
	public static Map<String, Object> reflectionToMap(final Object obj) {
		Map<String, Object> map = new HashMap<>();
		final Class<?> clazz = obj.getClass();
		reflectionByClass(obj, map, clazz);
        return map;
	}

	private static void reflectionByClass(final Object obj, Map<String, Object> map, final Class<?> clazz) {
		final Field[] fields = clazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
        for (final Field field : fields) {
            final String fieldName = field.getName();
            if (accept(field)) {
                try {
                    final Object fieldValue = field.get(obj);
                    if(fieldValue != null) {
                    	map.put(fieldName, fieldValue);
                    }
                } catch (final IllegalAccessException ex) {
                	ex.printStackTrace();
//                    throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                }
            }
        }
        Class superClz = clazz.getSuperclass();
        if(superClz != null) {
        	if(!superClz.getName().startsWith("java.")
        			&& !superClz.getName().startsWith("javax.")
        			&& !superClz.getName().startsWith("org.apache.")) {
        		reflectionByClass(obj, map, superClz);
        	}
        }
	}
	
	protected static boolean accept(final Field field) {
        if (field.getName().indexOf(ClassUtils.INNER_CLASS_SEPARATOR_CHAR) != -1) {
            // Reject field from inner class.
            return false;
        }
        if (Modifier.isTransient(field.getModifiers())) {
            // Reject transient fields.
            return false;
        }
        if (Modifier.isStatic(field.getModifiers())) {
            // Reject static fields.
            return false;
        }
        return true;
    }
}
