package org.ljdp;

import java.util.HashMap;

public class MapTest {

	public static void main(String[] args) {
		String t = "/module/aaaa";
		int i = t.indexOf("/", 1);
		System.out.println(t.substring(i, t.length()));
	}

}
