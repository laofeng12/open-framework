package org.ljdp;

import java.util.HashMap;

public class MapTest {

	public static void main(String[] args) {
		String t = "/module/aaaa";
		int i = t.indexOf("/", 1);
		System.out.println(t.substring(i, t.length()));
		
		String t2 = "0000000000000000000000000000000000000000";
		System.out.println(t2.replaceFirst("Bearer ", "").replaceFirst("bearer ", ""));
	}

}
