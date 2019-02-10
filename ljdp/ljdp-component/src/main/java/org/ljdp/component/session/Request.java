package org.ljdp.component.session;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class Request {
	private String sessionid;
	private Session session;
	private HashMap<String, String> params;
	private HashMap<String, List<Map<String, String>>> listParams;
	
	public Request(String sessionid) {
		if(sessionid != null) {
			sessionid = sessionid.trim();
		}
		this.sessionid = sessionid;
		params = new HashMap<String, String>();
		listParams = new HashMap<String, List<Map<String,String>>>();
	}
	
	public Session getSession() throws NullSessionException, SessionOverdueException {
		if(session == null) {
			session = SessionPool.getSession(sessionid);
		} else {
			session.updateConnectTime();
		}
		return session;
	}
	
	public void addParameter(String key, String value) {
		params.put(key, value);
	}
	
	public void addParameter(String key, List<Map<String, String>> listvalue) {
		listParams.put(key, listvalue);
	}
	
	public List<Map<String, String>> getParameters(String key){
		return listParams.get(key);
	}
	
	public String getParameter(String key) {
		String res = params.get(key);
		if(res == null) {
			res = "";
		}
		return res;
	}

	public String getSessionId() {
		return sessionid;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T toJavaBean(T bean) {
		Iterator<String> keyIt = params.keySet().iterator();
		while (keyIt.hasNext()) {
			String name = (String) keyIt.next();
			try {
				BeanUtils.setProperty(bean, name, params.get(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Iterator<String> listKeyIt = listParams.keySet().iterator();
		while (listKeyIt.hasNext()) {
			String name = (String) listKeyIt.next();
			try {
				String listItemClsName = null;
				Field f = bean.getClass().getDeclaredField(name);
				Type t = f.getGenericType();
				if(t instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType)t;
					if(pt.getRawType().toString().indexOf("java.util.List") != -1) {
						Type[] args = pt.getActualTypeArguments();
						if(args != null && args.length > 0) {
							listItemClsName = args[0].toString();
							listItemClsName = listItemClsName.replaceFirst("class", "").trim();
						}
					}
				}
				List list = new ArrayList();
				List<Map<String, String>> sublist = listParams.get(name);
				for (int i = 0; i < sublist.size(); i++) {
					Map<String, String> map = sublist.get(i);
					if(listItemClsName == null || listItemClsName.equals("java.lang.String")) {
						String val = map.get("text");
						if(val != null) {
							list.add(val);
						}
					} else {
						Class listItemClass = Class.forName(listItemClsName);
						Object item = listItemClass.newInstance();
						
						Iterator<String> subKeyIt = map.keySet().iterator();
						while (subKeyIt.hasNext()) {
							String key = (String) subKeyIt.next();
							BeanUtils.setProperty(item, key, map.get(key));
						}
						list.add(item);
					}
				}
				BeanUtils.setProperty(bean, name, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
}
