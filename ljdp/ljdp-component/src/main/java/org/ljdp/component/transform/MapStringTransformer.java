package org.ljdp.component.transform;

import java.util.Map;

public class MapStringTransformer implements StringTransformer {
	private Map<String, String> map;

	public MapStringTransformer(Map<String, String> map) {
		this.map = map;
	}

	public String transform(Object value) {
		if(value == null) {
			if(map.containsKey(null)) {
				return map.get(null);
			} else {
				return "";
			}
		}
		String key = value.toString().trim();
		if(map.containsKey(key)) {
			return map.get(key);
		}
		return key;
	}

}
