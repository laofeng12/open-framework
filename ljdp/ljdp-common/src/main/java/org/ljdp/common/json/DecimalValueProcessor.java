package org.ljdp.common.json;

import java.text.DecimalFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DecimalValueProcessor implements JsonValueProcessor {
	public static final String DEFAULT_DATE_PATTERN = "#.##";

	private DecimalFormat decimalFormat;

	/**
	 * 构造方法.
	 * 
	 * @param datePattern 日期格式
	 */
	public DecimalValueProcessor() {
		decimalFormat = new DecimalFormat(DEFAULT_DATE_PATTERN);
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if(value != null) {
			if(value instanceof Float) {
				return decimalFormat.format((Float) value);
			}
			if(value instanceof Double) {
				return decimalFormat.format((Double) value);
			}
			return value;
		}
		return "";
	}
}
