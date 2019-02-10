package org.ljdp.common.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class TimeValueProcessor implements JsonValueProcessor {

	public Object processArrayValue(Object value, JsonConfig arg1) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value, JsonConfig arg2) {
		return process(value);
	}
	
	private Object process(Object value) {
		if (value == null) {
			return null;
		}
		String time = "";
		if (value instanceof java.util.Date) {
			time = ((java.util.Date)value).getTime()+"";
		}
		if (value instanceof java.sql.Date) {
			time = ((java.sql.Date)value).getTime()+"";
		}
		if (value instanceof java.sql.Timestamp) {
			time = ((java.sql.Timestamp)value).getTime()+"";
		}
		return time;
	}

}
