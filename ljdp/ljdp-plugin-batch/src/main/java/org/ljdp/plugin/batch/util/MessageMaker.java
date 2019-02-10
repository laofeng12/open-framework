package org.ljdp.plugin.batch.util;

import java.text.MessageFormat;

import org.apache.commons.lang3.ArrayUtils;
import org.ljdp.common.bean.DynamicField;

public class MessageMaker {

	public static String makeMsg(int col, DynamicField field, String errMsg) {
		String msg = "[列数:{0}, 字段:{1}, 错误:{2}]";
		return MessageFormat.format(msg, col, field.getName_cn(), errMsg);
	}
	
	public static String makeInArrayDefinedMsg(String[] items) {
		String msg = "值必需是 {0} 中的一个";
		return MessageFormat.format(msg, ArrayUtils.toString(items));
	}
	
	public static String makeLengthLimitMsg(int maxLen, int currentLen) {
		String pattern = "定义的最大长度是:{0},当前长度是:{1}";
		return MessageFormat.format(pattern, maxLen, currentLen);
	}
	
	public static String makeCNLengthLimitMsg(int maxLen, int currentLen) {
		String pattern = "定义的最大长度是:{0},当前长度是:{1},最多支持中文字符个数:{2}";
		return MessageFormat.format(pattern, maxLen, currentLen, maxLen/2);
	}
	
	public static void main(String[] args) {
		System.out.println(makeInArrayDefinedMsg(new String[] {"a","b"}));
	}
}
