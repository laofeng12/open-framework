package org.ljdp.common.xml;

import java.util.HashMap;

import org.apache.commons.collections.Transformer;

public class HashMapTransformer implements Transformer {
	@SuppressWarnings("rawtypes")
	private HashMap codeMap;
	
	@SuppressWarnings("rawtypes")
	public HashMapTransformer(HashMap map) {
		this.codeMap = map;
	}
	
	public Object transform(Object key) {
		if(codeMap != null) {
			if(codeMap.containsKey(key)) {	
				return codeMap.get(key);
			}
			String strval = key.toString();
			if(codeMap.containsKey(strval)){
				return codeMap.get(strval);
			}
			if(key instanceof String) {
				try {
					Integer intval = new Integer(Integer.parseInt((String)key));
					if(codeMap.containsKey(intval)) {
						return codeMap.get(intval);
					}
				} catch(Exception e) {
					
				}
			}
		}
		return key;
	}

}
