package org.ljdp.common.xml;

import org.dom4j.Document;
import org.dom4j.Element;

public class XMLResponseFactory {
	public static final String CODE_SUCCESS = "1";//成功
	public static final String CODE_FAIL = "-1";//失败
	public static final String CODE_NOTLOGIN = "-2";//用户没有登录
	public static final String CODE_BUSY = "-3";//服务器繁忙，用户数已达到最大值
	
	private BeanToXML beanToXML;
	
	public XMLResponseFactory() {
		beanToXML = new ReflectBeanToXML();
	}
	
	public XMLResponseFactory(BeanToXML beanToXML) {
		this.beanToXML = beanToXML;
	}
	
	public Document createResponseDOM(String code, String msg, Object result) {
		Document doc = XMLUtils.createDocument("response", new String[] {"code","message","results"});
		Element root = doc.getRootElement();
		root.element("code").setText(code);
		root.element("message").setText(msg);
		Element[] resEl = beanToXML.toXMLElement(result);
		if(resEl != null) {
			for(int j = 0; j < resEl.length; ++j) {
				root.element("results").add(resEl[j]);
			}
		}
		return doc;
	}
	
	public Document createResponseDOM(String code, String msg, Object[] results) {
		Document doc = XMLUtils.createDocument("response", new String[] {"code","message","results"});
		Element root = doc.getRootElement();
		root.element("code").setText(code);
		root.element("message").setText(msg);
		if(results != null) {
			for(int i = 0; i < results.length; ++i) {
				Object r = results[i];
				if(r != null) {
					if(r instanceof Element) {
						root.element("results").add((Element)r);
					} else {
						Element[] resEl = beanToXML.toXMLElement(results);
						if(resEl != null) {
							for(int j = 0; j < resEl.length; ++j) {
								root.element("results").add(resEl[j]);
							}
						}
					}
				}
			}
		}
		return doc;
	}
	
	public String createResponse(String code, String msg, Object result) {
		Document doc = createResponseDOM(code, msg, result);
//		System.out.println(XMLUtils.asXMLString(doc));
//		EncodeUtils.setSystemEncodeToCN();
//		System.out.println(doc.asXML());
//		System.out.println("测试中文问题...");
		return doc.asXML();
	}
	
	public String createResponse(String code, String msg, Object[] results) {
		Document doc = createResponseDOM(code, msg, results);
		return doc.asXML();
	}
	
	public String createSuccessResponse(Object result) {
		return createResponse(CODE_SUCCESS, "", result);
	}
	
	public String createFailResponse(String error) {
		return createResponse(CODE_FAIL, error);
	}
	
	public String createNotLoginResponse() {
		return createResponse(CODE_NOTLOGIN, "用户没有登录");
	}
	
	public String createOverdueResponse() {
		return createResponse(CODE_NOTLOGIN, "用户登录已超时");
	}
	
	public String createBusyResponse(String msg) {
		return createResponse(CODE_BUSY, msg);
	}
	
	public String createBusyResponse() {
		return createBusyResponse("服务器繁忙，请稍候再试");
	}
	
	public String createResponse(String code, String msg) {
		return createResponse(code, msg, null);
	}
}
