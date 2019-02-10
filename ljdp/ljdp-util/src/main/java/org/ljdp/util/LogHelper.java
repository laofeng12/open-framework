package org.ljdp.util;

import org.apache.commons.lang3.ArrayUtils;

public class LogHelper {
	
	/**
	 * 输出调用方法的参数
	 * @param pNames 参数名
	 * @param args 参数值
	 */
	public static String param(String[] pNames, Object[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append("parameters:{");
		for(int i = 0; i < args.length; ++i) {
			Object p = args[i];
			sb.append(pNames[i]).append(":")
				.append(ArrayUtils.toString(p)).append(",");
		}
		if(args.length > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 输出调用方法的参数, 参数名以p1 p2 p3...表示
	 * @param args
	 */
	public static String param(Object... args) {
		String[] pNames = new String[args.length];
		for(int i = 0; i < pNames.length; ++i) {
			pNames[i] = "p" + (i + 1);
		}
		return param(pNames, args);
	}
}
