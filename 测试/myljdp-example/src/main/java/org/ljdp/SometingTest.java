package org.ljdp;

public class SometingTest {

	public static void main(String[] args) {
		Integer code1 = new Integer(300);
		Integer code2 = null;
		Integer code3 = new Integer(200);
		System.out.println(Integer.valueOf(200).equals(code1));
		System.out.println(Integer.valueOf(200).equals(code2));
		System.out.println(Integer.valueOf(200).equals(code3));
	}

}
