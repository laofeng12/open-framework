package org.ljdp.common.xml;

import org.dom4j.Element;

public interface BeanToXML {
	public Element[] toXMLElement(Object bean);
}
