package org.ljdp.secure.cipher;

import java.security.NoSuchAlgorithmException;

public class SHATest {

	public static void main(String[] args) {
		try {
			
			System.out.println(SHA256.encodeAsBase64("ioc20190717"));//ssodemo
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

}
