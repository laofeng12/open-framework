package org.ljdp.common.xml;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ljdp.util.DateFormater;

@SuppressWarnings({"unchecked","rawtypes"})
public class MapToXML implements BeanToXML {
	protected FastDateFormat defaultDF = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	/**
	 * 忽略的字段树，不转换为XML
	 * 相当于Map<String, Map>()
	 */
	protected HashMap ignorePropertys = new HashMap();//相当于Hashtable<String, Hashtable>()
	/**
	 * 生成XML的根元素,默认用对象名
	 */
	protected String rootText;//生成XML的根元素,默认用对象名
	/**
	 * 自定义的包，属于此包名下的对象才可以继续进行ReflectBeanToXML。
	 * 防止对java.*,org.apache.*等通用类进行ReflectBeanToXML
	 */
	protected String customizeBeanNameSpace = "cn.com.sunrise";
	/**
	 * 转换器列表
	 */
	protected HashMap transformers = new HashMap();
	protected boolean needRootEl = true;
	protected boolean upperCaseFirstChar = false;
	protected boolean autoItemName = false;
	
	public MapToXML() {
		addIgnoreProperty("class");
		addIgnoreProperty("Class");
	}
	
	public MapToXML(String root) {
		this();
		rootText = root;
	}
	
	private class TransformerResult {
		private Object value;
		private Map subTransf;
		
		public Map getSubTransf() {
			return subTransf;
		}
		public void setSubTransf(Map subTransf) {
			this.subTransf = subTransf;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	/**
	 * 生成XML时忽略的属性。
	 * 支持嵌套属性，即如果属性是一个自定义的对象，可忽略此对象中的属性。
	 * 例如对象中有一个自定义的对象属性user，想忽略user中的sex属性，
	 * 则可以addIgnoreProperty("user.sex")
	 * 此方法对于集合对象同样有效，例如users是一个Collection<User>，即是user对象的集合，
	 * 则可以addIgnoreProperty("users.sex")
	 * @param name
	 */
	public void addIgnoreProperty(String name) {
		if(StringUtils.isBlank(name)) {
			return;
		}
		String[] items = StringUtils.split(name, ".");
		HashMap subPropers = subPropertys(ignorePropertys, items);
		ignorePropertys.put(items[0], subPropers);
	}
	
	public void addIgnoreProperty(Map ignores) {
		if(ignores != null) {			
			ignorePropertys.putAll(ignores);
		}
	}

	private HashMap subPropertys(HashMap currentPropers, String[] items) {
		HashMap subPropers = null;
		if(items.length > 1) {
			if(currentPropers.containsKey(items[0])) {
				subPropers = (HashMap)currentPropers.get(items[0]);
			}
			if(subPropers == null) {
				subPropers = new HashMap();
			}
			String[] subItems = (String[])ArrayUtils.subarray(items, 1, items.length);
			HashMap thisSubPropers = subPropertys(subPropers, subItems);
			subPropers.put(items[1], thisSubPropers);
		}
		return subPropers;
	}
	
	public void addTransformer(String name, String format) {
		addObjectTransformer(name, format);
	}
	
	public void addTransformer(String name, Transformer transform) {
		addObjectTransformer(name, transform);
	}
	
	public void addTransformer(String name, HashMap transMap) {
		addObjectTransformer(name, new HashMapTransformer(transMap));
	}
	
	public void addTransformer(Map transMap) {
		if(transMap != null) {
			transformers.putAll(transMap);
		}
	}
	
	protected void addObjectTransformer(String name, Object transform) {
		if(StringUtils.isBlank(name)) {
			return;
		}
		String[] items = StringUtils.split(name, ".");
		Object subtrans = getSubTransformer(transformers, items, transform);
		transformers.put(items[0], subtrans);
	}
	
	private Object getSubTransformer(HashMap node, String[] items, Object transform) {
		if(items.length > 1) {
			HashMap subnode = new HashMap();
			if(node.containsKey(items[0])) {
				Object val = node.get(items[0]);
				if(val instanceof HashMap) {
					subnode = (HashMap)val;
				}
			}
			String[] subItems = (String[])ArrayUtils.subarray(items, 1, items.length);
			Object subTrans = getSubTransformer(subnode, subItems, transform);
			subnode.put(items[1], subTrans);
			return subnode;
		} else {
			return transform;
		}
	}
	
	/**
	 * 此属性是否被忽略
	 * @param key
	 * @return
	 */
	protected boolean isIgnoreProperty(String key) {
		if(ignorePropertys.containsKey(key)) {
			HashMap subPropers = (HashMap)ignorePropertys.get(key);
			if(subPropers == null || subPropers.size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 此属性是否有被忽略的子属性
	 * @param key
	 * @return
	 */
	protected boolean isContainsIgnoreProperty(String key) {
		if(ignorePropertys.containsKey(key)) {
			HashMap subPropers = (HashMap)ignorePropertys.get(key);
			if(subPropers != null && subPropers.size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否自定义的对象
	 * @param bean
	 * @return
	 */
	protected boolean isCustomizeBean(Object bean) {
		if(bean != null && bean.getClass().getName().startsWith(customizeBeanNameSpace)) {
			return true;
		}
		return false;
	}

	public Element[] toXMLElement(Object bean) {
		if(bean == null) {
			return null;
		}
		Map<String, Object> beanDesc = (Map<String, Object>)bean;
		Iterator<String> it = beanDesc.keySet().iterator();
		ArrayList<String> fieldList = new ArrayList<String>();
		while(it.hasNext()) {
			String key = it.next();
			String keyName = key;
			if(isUpperCaseFirstChar()) {
				keyName = key.substring(0, 1).toUpperCase()+key.substring(1);
			}
			if(isIgnoreProperty(keyName)) {
				continue;
			}
			if(beanDesc.get(key) != null) {
				fieldList.add(keyName);
			}
		}
		String[] fields = (String[])fieldList.toArray(new String[] {});
		if(StringUtils.isBlank(rootText)) {
			rootText = bean.getClass().getSimpleName();
		}
		Element rootEl = XMLUtils.createElement(rootText, fields);
		for(int i = 0; i < fields.length; ++i) {
			String key = fields[i];
			String tempkey = key;
			if(isUpperCaseFirstChar()) {
				tempkey = key.substring(0, 1).toLowerCase()+key.substring(1);					
			}
			Object value = beanDesc.get(tempkey);
			Object valueEl = createElement(key, value);
			if(valueEl != null && valueEl instanceof Element) {
				rootEl.remove(rootEl.element(key));
				rootEl.add((Element)valueEl);
			} else {
				rootEl.element(key).setText(valueEl.toString());
			}
		}
		Element[] els = toElementArray(rootEl);
		return els;
	}

	private Object createElement(String key, Object value) {
		Object valueEl = null;
		if(value instanceof java.util.Date) {
			Date date = (Date)value;
			valueEl = formateDate(key, date);
		} else if(value instanceof java.sql.Date) {
			java.sql.Date sqlDate = (java.sql.Date)value;
			Date date = new Date(sqlDate.getTime());
			valueEl = formateDate(key, date);
		} else if(value instanceof java.sql.Timestamp) {
			Timestamp time = (Timestamp)value;
			Date date = new Date(time.getTime());
			valueEl = formateDate(key, date);
		} else if(value instanceof java.util.Collection) {
			valueEl = transformCollection(key, value);
		} else if(value instanceof java.util.Map) {
			valueEl = transformMap(key, value);
		} else if(isCustomizeBean(value)) {
			valueEl = transformBean(key, value);
		} else {
			valueEl = transformCommon(key, value);
		}
		return valueEl;
	}

	protected Element[] toElementArray(Element rootEl) {
		Element[] els;
		if(needRootEl) {
			els = new Element[] {rootEl};
		} else {
			List rootElements = rootEl.elements();
			els = new Element[rootElements.size()];
			for(int i = 0; i < rootElements.size(); ++i) {
				Element el = (Element)rootElements.get(i);
				els[i] = el;
				rootEl.remove(el);
			}
		}
		return els;
	}

	public Document toXML(Object bean) {
		Element[] els = toXMLElement(bean);
		Document document = DocumentHelper.createDocument();
		document.add(els[0]);
		return document;
	}

	protected TransformerResult transformValue(String key, Object value) {
		TransformerResult tfRes = new TransformerResult();
		if(transformers.containsKey(key)) {
			Object transf = transformers.get(key);
			if(transf instanceof Transformer) {
				Transformer mytran = (Transformer)transf;
				tfRes.setValue(mytran.transform(value));
			} else if(transf instanceof Map) {
				tfRes.setSubTransf((Map)transf);
			}
		}
		return tfRes;
	}
	
	protected Object transformCommon(String key, Object value) {
		if(value == null) {
			return "";
		}
		TransformerResult tfRes = transformValue(key, value);
		if(tfRes.getValue() != null) {
			return tfRes.getValue();
		}
		return value.toString();
	}
	
	protected Object transformMap(String key, Object value) {
		TransformerResult tfRes = transformValue(key, value);
		if(tfRes.getValue() != null) {
			return tfRes.getValue();
		}
		MapToXML beanToXML = new MapToXML(key);
		setChildIgnoreProperty(key, beanToXML);
		beanToXML.addTransformer(tfRes.getSubTransf());
		beanToXML.setUpperCaseFirstChar(this.isUpperCaseFirstChar());
		beanToXML.setAutoItemName(this.isAutoItemName());
		return beanToXML.toXMLElement(value)[0];
	}
	
	protected Object transformCollection(String key, Object value) {
		TransformerResult tfRes = transformValue(key, value);
		Map subTrans = null;
		if(tfRes.getValue() != null) {
			return tfRes.getValue();
		} else {
			subTrans = tfRes.getSubTransf();
		}
		Transformer commonTrans = null;
		if(subTrans != null && subTrans.containsKey("*")) {
			Object tempTran = subTrans.get("*");
			if(tempTran instanceof Transformer) {
				commonTrans = (Transformer)tempTran;
			}
		}
		Element valueEl = XMLUtils.createElement(key, new String[] {});
		String itemName = "item";
		if(isAutoItemName()) {
			itemName = key.substring(0, key.length()-1);
		}
		ReflectBeanToXML beanToXML = new ReflectBeanToXML(itemName);
		setChildIgnoreProperty(key, beanToXML);
		beanToXML.addTransformer(subTrans);
		beanToXML.setUpperCaseFirstChar(this.isUpperCaseFirstChar());
		beanToXML.setAutoItemName(this.isAutoItemName());
		beanToXML.setCustomizeBeanNameSpace(this.customizeBeanNameSpace);
		Iterator iter = ((Collection)value).iterator();
		while(iter.hasNext()) {
			Object itemVal = iter.next();
			Element itemEl;
			if(commonTrans != null) {
				Object result = commonTrans.transform(itemVal);
				if(result instanceof Element) {
					itemEl = (Element)result;
				} else {
					itemEl = XMLUtils.createElement("item", result.toString());
				}
			} else if(isCustomizeBean(itemVal)) {
				itemEl = beanToXML.toXMLElement(itemVal)[0];
			} else {
				itemEl = XMLUtils.createElement("item", itemVal.toString());
			}
			valueEl.add(itemEl);
		}
		return valueEl;
	}

	protected Object transformBean(String key, Object value) {
		TransformerResult tfRes = transformValue(key, value);
		if(tfRes.getValue() != null) {
			return tfRes.getValue();
		}
		ReflectBeanToXML beanToXML = new ReflectBeanToXML(key);
		setChildIgnoreProperty(key, beanToXML);
		beanToXML.addTransformer(tfRes.getSubTransf());
		beanToXML.setUpperCaseFirstChar(this.isUpperCaseFirstChar());
		beanToXML.setAutoItemName(this.isAutoItemName());
		beanToXML.setCustomizeBeanNameSpace(this.customizeBeanNameSpace);
		return beanToXML.toXMLElement(value)[0];
	}

	protected String formateDate(String key, Date date) {
		String valueStr;
		if(transformers.containsKey(key)) {
			Object tranf = transformers.get(key);
			if(tranf instanceof String) {
				valueStr = DateFormater.formatDate(date, (String)tranf);
			} else if(tranf instanceof Transformer) {
				valueStr = (String)((Transformer)tranf).transform(date);
			} else {
				valueStr = defaultDF.format(date);
			}
		} else {						
			valueStr = defaultDF.format(date);
		}
		return valueStr;
	}

	protected void setChildIgnoreProperty(String key, MapToXML beanToXML) {
		if(isContainsIgnoreProperty(key)) {
			HashMap ignorePros = (HashMap)ignorePropertys.get(key);
			beanToXML.addIgnoreProperty(ignorePros);
		}
	}

	public HashMap getIgnorePropertys() {
		return ignorePropertys;
	}

	public void setIgnorePropertys(HashMap ignorePropertys) {
		this.ignorePropertys = ignorePropertys;
	}

	public HashMap getTransformers() {
		return transformers;
	}

	public void setTransformers(HashMap transformers) {
		this.transformers = transformers;
	}

	public String getCustomizeBeanNameSpace() {
		return customizeBeanNameSpace;
	}

	public void setCustomizeBeanNameSpace(String customizeBeanNameSpace) {
		this.customizeBeanNameSpace = customizeBeanNameSpace;
	}
	
	public void setDateFormat(String format) {
		defaultDF = FastDateFormat.getInstance(format);
	}

	public boolean isNeedRootEl() {
		return needRootEl;
	}

	public void setNeedRootEl(boolean needRootEl) {
		this.needRootEl = needRootEl;
	}

	public boolean isUpperCaseFirstChar() {
		return upperCaseFirstChar;
	}

	public void setUpperCaseFirstChar(boolean upperCaseFirstChar) {
		this.upperCaseFirstChar = upperCaseFirstChar;
	}

	public boolean isAutoItemName() {
		return autoItemName;
	}

	public void setAutoItemName(boolean autoItemName) {
		this.autoItemName = autoItemName;
	}

}
