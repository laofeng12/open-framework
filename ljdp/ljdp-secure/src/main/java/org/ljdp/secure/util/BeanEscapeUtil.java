package org.ljdp.secure.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.ljdp.secure.annotation.IgnoreEscape;
import org.ljdp.util.EmojiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanEscapeUtil {
	private static Logger log = LoggerFactory.getLogger("Security");
	
	private static final Map<Class, Set<String>> global_ignores = new HashMap<>();//忽略的类的属性
	private static final Map<Class, Set<String>> emoji_suppor = new HashMap<>();//支持emoji
	private static final Map<Class, Set<String>> emoji_filter = new HashMap<>();//支持emoji
	
	public static void addIgnore(Class clz, String field) {
		if(global_ignores.containsKey(clz)) {
			global_ignores.get(clz).add(field);
		} else {
			Set<String> sets = new HashSet<>();
			sets.add(field);
			global_ignores.put(clz, sets);
		}
	}
	public static void addEmojiSuppor(Class clz, String field) {
		if(emoji_suppor.containsKey(clz)) {
			emoji_suppor.get(clz).add(field);
		} else {
			Set<String> sets = new HashSet<>();
			sets.add(field);
			emoji_suppor.put(clz, sets);
		}
	}
	public static void addEmojiFilter(Class clz, String field) {
		if(emoji_filter.containsKey(clz)) {
			emoji_filter.get(clz).add(field);
		} else {
			Set<String> sets = new HashSet<>();
			sets.add(field);
			emoji_filter.put(clz, sets);
		}
	}

	public static void escapeBean(final Object obj, boolean force,boolean filterJs, boolean filterQuotes) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(obj == null) {
			return;
		}
		Class clz = obj.getClass();
		if(obj instanceof Map) {
			Map map = (Map)obj;
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				Object key = (Object) it.next();
				Object inobj = map.get(key);
				if(inobj instanceof String) {
					map.put(key, escapeIfNotSecurity(clz, key.toString(), (String)inobj, force, filterJs, filterQuotes));
				} else {
					escapeBean(inobj, force, filterJs, filterQuotes);
				}
			}
		} else if(obj instanceof List) {
			List list = (List)obj;
			for (int i = 0; i < list.size(); i++) {
				Object inobj = (Object) list.get(i);
				if(inobj instanceof String) {
					String valstr = escapeIfNotSecurity(clz, i+"", (String)inobj, force, filterJs, filterQuotes);
					list.set(i, valstr);
				} else {
					escapeBean(inobj, force, filterJs, filterQuotes);
				}
			}
		} else if(isCustomerClass(clz)) {
			Set<String> ignoreFields = global_ignores.get(clz);
			Set<String> emoji_filter_clz = emoji_filter.get(clz);
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String fn = field.getName();
				boolean needSecure = true, filterEmoji = false;
				if(ignoreFields != null) {
					if(ignoreFields.contains(fn)) {
						needSecure = false;
					}
				}
				IgnoreEscape ignore = field.getAnnotation(IgnoreEscape.class);
				if(ignore != null) {
					needSecure = false;
				}
				if(emoji_filter_clz != null) {
					if(emoji_filter_clz.contains(fn)) {
						filterEmoji = true;
					}
				}
				if(!needSecure && !filterEmoji) {
					continue;
				}
				String methodF = fn.substring(0, 1).toUpperCase()+fn.substring(1, fn.length());
				String getName = "get"+methodF;
				try {
					Method getMethod = clz.getMethod(getName, null);
					Object val = getMethod.invoke(obj, null);
					if(val instanceof String) {
						String setName = "set"+methodF;
						try {
							Method setMethod = clz.getMethod(setName, String.class);
							String valstr = (String)val;
							if(filterEmoji) {
								valstr = EmojiUtil.resolveToEmptyFromEmoji(valstr);
							}
							if(needSecure) {
								valstr = escapeIfNotSecurity(clz, fn, valstr, force, filterJs, filterQuotes);
							} else {
								valstr = filterScript(valstr);
							}
							setMethod.invoke(obj, valstr);
						} catch (NoSuchMethodException e) {
						}
					} else {
						Object inobj = getMethod.invoke(obj, null);
						escapeBean(inobj, force, filterJs, filterQuotes);
					}
				} catch (NoSuchMethodException e) {
//					e.printStackTrace();
				}
			}
		}
	}
	
	private static boolean isCustomerClass(final Class clz) {
		String ttype = clz.getName();
		if(ttype.startsWith("com.ecp") || ttype.startsWith("com.liumu")) {
			return true;
		}
		return false;
	}
	
	public static String escapeIfNotSecurity(Class rootClz, String fieldName, final String val, boolean force,
			boolean filterJs, boolean filterQuotes) {
		if(val == null) {
			return val;
		}
		String tmp = val;
		if(filterJs) {
			tmp = filterScript(tmp);
		}
		if(filterQuotes) {
			tmp = filterQuotes(tmp);
		}
		if(force) {
			return StringEscapeUtils.escapeHtml4(tmp);
		} else {
			boolean lt = (tmp.indexOf('<') > -1);
			boolean gt = (tmp.indexOf('>') > -1);
			boolean containJs = false;
			boolean containBody = false;
			if(tmp.indexOf("<script") != -1 || tmp.indexOf("<SCRIPT") != -1
					|| tmp.indexOf("</script>") != -1 || tmp.indexOf("</SCRIPT>") != -1) {
				containJs = true;
			}
			if(tmp.indexOf("<body") != -1 || tmp.indexOf("<BODY") != -1
					|| tmp.indexOf("</body>") != -1 || tmp.indexOf("</BODY>") != -1) {
				containBody = true;
			}
			if(lt && gt && ( containJs || containBody)) {
				return StringEscapeUtils.escapeHtml4(tmp);
			}
//			if(containBody || containJs || (lt && gt)) {
//				log.info("【可疑】对象："+rootClz.getName()+",属性："+fieldName+",数据："+val);
//			}
		}
		return tmp;
	}
	
	public static String filterScript(String val) {
		val = val.replaceAll("<script>", "");
		val = val.replaceAll("<script", "");
		val = val.replaceAll("</script>", "");
		val = val.replaceAll("<body", "");
		val = val.replaceAll("</body>", "");
		return val;
	}
	
	public static String filterQuotes(String val) {
		val = val.replaceAll("'", "");
		val = val.replaceAll("\"", "");
		return val;
	}
}
