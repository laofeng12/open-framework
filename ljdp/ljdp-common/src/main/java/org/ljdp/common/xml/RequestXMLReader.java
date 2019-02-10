package org.ljdp.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.ljdp.component.session.Request;
import org.ljdp.component.session.RequestErrorException;

public class RequestXMLReader {
	public static Request parseXML(String xml) throws RequestErrorException {
		try {
			Document doc = XMLUtils.getDocument(xml);
			Element reqel = doc.getRootElement();
			Request request = new Request(reqel.elementText("sessionid"));
			Element paramEL = reqel.element("parameters");
			if(paramEL != null) {
				Iterator<?> paramIT = paramEL.elementIterator();
				while(paramIT.hasNext()) {
					Element el = (Element)paramIT.next();
					Element itemsEl = el.element("items");
					if(itemsEl != null) {
						List<Map<String, String>> list = new ArrayList<Map<String,String>>();
						Iterator<?> it = itemsEl.elementIterator();
						while (it.hasNext()) {
							Element itemEl = (Element) it.next();
							if(itemEl.getName().equals("item")) {
								Map<String, String> subItemMap = new HashMap<String, String>();
								Iterator<?> subIt = itemEl.elementIterator();
								while (subIt.hasNext()) {
									Element subEl = (Element) subIt.next();
									subItemMap.put(subEl.getName(), subEl.getText());
								}
								if(subItemMap.isEmpty()) {
									subItemMap.put("text", itemEl.getText());
								}
								list.add(subItemMap);
							}
						}
						request.addParameter(el.getName(), list);
					} else {
						request.addParameter(el.getName(), el.getText());
					}
				}
			}
			return request;
		} catch (Exception e) {
			throw new RequestErrorException(e);
		}
	}
	
}
