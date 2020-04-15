package org.ljdp;

import java.util.UUID;

import org.ljdp.component.sequence.ConcurrentSequence;

public class SometingTest {

	public static void main(String[] args) {
		Integer code1 = new Integer(300);
		Integer code2 = null;
		Integer code3 = new Integer(200);
		System.out.println(Integer.valueOf(200).equals(code1));
		System.out.println(Integer.valueOf(200).equals(code2));
		System.out.println(Integer.valueOf(200).equals(code3));
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
		System.out.println(UUID.randomUUID().toString());
	}

}
