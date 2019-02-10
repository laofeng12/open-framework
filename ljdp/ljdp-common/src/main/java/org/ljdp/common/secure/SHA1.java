package org.ljdp.common.secure;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class SHA1 {
	protected static String toHexString(byte[] digest) {
		StringBuffer hexstr = new StringBuffer();
		String shaHex = "";
		for (int i = 0; i < digest.length; i++) {
			shaHex = Integer.toHexString(digest[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		return hexstr.toString();
	}
	
	public static byte[] encode(String data, String charsetName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		messageDigest.update(data.getBytes(charsetName));
		return messageDigest.digest();
	}
	
	public static String encodeAsString(String data, String charsetName) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(data.getBytes(charsetName));
			byte[] md5Bytes = messageDigest.digest();
			return toHexString(md5Bytes);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static String encodeAsBase64(String data, String charsetName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return Base64.encodeBase64String(encode(data, charsetName));
	}
	
//	public static void main(String[] args) {
//		System.out.println(SHA1.encode("abc"));
//	}
}
