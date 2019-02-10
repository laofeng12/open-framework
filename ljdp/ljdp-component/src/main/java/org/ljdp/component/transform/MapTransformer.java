package org.ljdp.component.transform;

import java.util.Map;

import org.apache.commons.collections.Transformer;

@SuppressWarnings("rawtypes")
public class MapTransformer implements Transformer {
	private Map map;

	public MapTransformer(Map map) {
		this.map = map;
	}

	public Object transform(Object arg0) {
		return map.get(arg0);
	}

}
