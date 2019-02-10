package org.ljdp.util;

import java.util.Hashtable;

public class SimpleBeanContext {
	private static Hashtable<String, Object> beans = new Hashtable<String, Object>();
	
	public static Object getSingleBean(String className) {
		if( !beans.containsKey(className) ) {
			try {
				Object bean = Class.forName(className).newInstance();
				beans.put(className, bean);
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return beans.get(className);
	}
}
