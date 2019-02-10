package org.ljdp.plugin.batch.util;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.bean.FieldType;
import org.ljdp.plugin.batch.check.FormatException;
import org.ljdp.util.DateFormater;

public class BaseTypeUtils {
	
	public static void check(int col, DynamicField field, String value)
			throws FormatException {
		try {
			String baseType = field.getBaseType();
			if(baseType != null 
					&& (baseType.equals(FieldType.BASE_STRING) || baseType.equals(FieldType.BASE_CHAR))) {
				if(field.getMaxLength() > 0) {
					if(StringUtils.isNotBlank(field.getEncode())) {
						value = new String(value.getBytes("GBK"), field.getEncode());
					}
					if(value.length() > field.getMaxLength()) {
						String msg;
						if(field.getEncode() != null 
								&& field.getEncode().toUpperCase().equals("ISO8859-1")) {
							msg = MessageMaker.makeCNLengthLimitMsg(field.getMaxLength(), value.length());
						} else {
							msg = MessageMaker.makeLengthLimitMsg(field.getMaxLength(), value.length());
						}
						throw new FormatException(msg);
					}
				}
			} else {				
				parseObject(field, value);
			}
		} catch(NumberFormatException ne) {
			throw new FormatException(MessageMaker.makeMsg(col, field, "只能是数字类型"));
		} catch(ParseException e) {
			throw new FormatException(MessageMaker.makeMsg(col, field, "转换日期出错，定义的日期格式为：" + field.getCustomizeType()));
		} catch (Exception e) {
			throw new FormatException(MessageMaker.makeMsg(col, field, e.getMessage()));
		}
	}

	public static Object parseObject(DynamicField field, String value) throws ParseException
			 {
		if(StringUtils.isBlank(value)) {
			return "";
		}
		String baseType = field.getBaseType();
		if (StringUtils.isNotEmpty(baseType)) {
			if(baseType.equals(FieldType.BASE_STRING) || baseType.equals(FieldType.BASE_CHAR)) {
				return value.trim();
			} else if (baseType.equals(FieldType.BASE_NUMBER) && field.getDecimalRange() <= 0) {
				return Long.parseLong(value);
			} else if (baseType.equals(FieldType.BASE_FLOAT) 
					|| ( baseType.equals(FieldType.BASE_NUMBER) && field.getDecimalRange() > 0 )) {
				return Float.parseFloat(value);
			} else if (baseType.equals(FieldType.BASE_DATE) 
					|| baseType.equals(FieldType.BASE_TIMESAMP)) {
				String customizeType = field.getCustomizeType();
				value = value.trim();
				FastDateFormat sdf = DateFormater.getDateFormat(value, customizeType);
				return sdf.parse(value);
			}
		}
		return value;
	}

}
