package org.ljdp.secure.cipher;

import org.apache.commons.codec.binary.Base64;

public class TestCase {
	public static void main(String[] args) {
		String ecbase64 = Base64.encodeBase64URLSafeString("liumu-jr-by-zy".getBytes());
		System.out.println(ecbase64);
		System.out.println(ecbase64.substring(0, 16));
		
		System.out.println("=============");
		try {
			ecbase64 = Base64.encodeBase64URLSafeString(SHA1.encode("liumu-jr-by-zy"));
			System.out.println(ecbase64);
			System.out.println(ecbase64.substring(0, 16));
			
			System.out.println(SHA256.encodeAsBase64("datalakeadmin"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
