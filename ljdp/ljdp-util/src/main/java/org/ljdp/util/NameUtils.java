package org.ljdp.util;


public class NameUtils {

	/**
	 * 把字段名的‘_’去掉并把‘_’后第一个字符大写
	 * @param column
	 * @return
	 */
	public static String columnTojavaName(String column) {
		String res = column.toLowerCase();
		while(res.indexOf("_") != -1) {
			char a = res.charAt(res.indexOf("_") + 1);
			res = res.replaceFirst("_"+a, Character.toUpperCase(a) + "");
		}
		return res;
	}

	/**
	 * 第一个字符大写
	 * @param str
	 * @return
	 */
	public static String toUpperCaseFirst(String str) {
		char f = str.charAt(0);
		str = str.replaceFirst(f + "", Character.toUpperCase(f) + "");
		return str;
	}
	
	public static String toLowerCaseFirst(String str) {
		char f = str.charAt(0);
		str = str.replaceFirst(f + "", Character.toLowerCase(f) + "");
		return str;
	}

	/**
	 * 把字段名转换为page前缀的字段名
	 * @param name
	 * @return
	 */
	public static String toPageName(String name) {
		return "page" + toUpperCaseFirst(name);
	}
	
	/**
	 * 取包名前缀
	 * @param cls
	 * @param len
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public static String getPackageName(Class cls, int len) {
		String[] items = cls.getName().split("\\.");
		String ns = "";
		for(int i = 0; i < items.length && i < len; ++i) {
			if(ns.length() > 0) {
				ns += ".";
			}
			ns += items[i];
		}
		return ns;
	}
	
	public static void main(String[] args) {
		System.out.println(NameUtils.columnTojavaName("MNY_NAME"));
		System.out.println(NameUtils.toUpperCaseFirst("aabcab"));
		System.out.println(NameUtils.toLowerCaseFirst("Abc"));
		System.out.println(NameUtils.toPageName("aabcab"));
		System.out.println(NameUtils.getPackageName(NameUtils.class, 2));
	}
}
