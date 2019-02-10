package org.ljdp.util;

import java.io.IOException;
import java.io.InputStream;

public class StringUtil {
	public static String fromInputStream(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
	
	public static String upperFirst(String val) {
		return val.substring(0, 1).toUpperCase()+val.substring(1, val.length());
	}
	
	public static String lowerFirst(String val) {
		return val.substring(0, 1).toLowerCase()+val.substring(1, val.length());
	}
	
//	public static void main(String[] args) {
//		System.out.println(StringUtil.upperFirst("abcdd"));
//	}
}
