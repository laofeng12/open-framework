package org.ljdp.common.xml;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class ReflectBeanToXML extends MapToXML {
	
	public ReflectBeanToXML() {
		super();
	}
	
	public ReflectBeanToXML(String root) {
		super(root);
	}

	public Element[] toXMLElement(Object bean) {
		if(bean == null) {
			return null;
		}
		if(bean instanceof org.dom4j.Element) {
			return new Element[] { (Element)bean };
		}
		if(StringUtils.isBlank(rootText)) {
			rootText = bean.getClass().getSimpleName();
		}
		if( !super.isCustomizeBean(bean) ) {
			Element el = XMLUtils.createElement(rootText, bean.toString());
			return new Element[] {el};
		}
		try {
			@SuppressWarnings("rawtypes")
			Map beanDesc = PropertyUtils.describe(bean);
			return super.toXMLElement(beanDesc);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

}
