package org.ljdp.util;

import java.util.ArrayList;
import java.util.List;

public class MyArrayUtils {
	@SuppressWarnings("rawtypes")
	public static String toString(List list, String separated) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < list.size(); ++i) {
			if( sb.length() > 0 ) {
				sb.append(separated);
			}
			Object o = list.get(i);
			if(o instanceof String) {
				sb.append((String)o);
			} else {
				sb.append(o.toString());
			}
		}
		return sb.toString();
	}
	
	public static List<String> toList(String[] array){
		ArrayList<String> list = new ArrayList<String>();
		if(array != null) {
			for (int i = 0; i < array.length; i++) {
				list.add(array[i]);
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		System.out.println(MyArrayUtils.toString(list, ","));
	}
}
