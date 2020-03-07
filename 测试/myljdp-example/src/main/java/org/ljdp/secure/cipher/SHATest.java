package org.ljdp.secure.cipher;

import java.security.NoSuchAlgorithmException;

public class SHATest {

	public static void main(String[] args) {
		try {
			System.out.println(SHA256.encodeAsBase64("Fypaicha#2020"));//ssodemo
			System.out.println(SHA256.encodeAsBase64("Dgioc#2020"));
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}
