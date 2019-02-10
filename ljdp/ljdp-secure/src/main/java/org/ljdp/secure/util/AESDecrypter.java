package org.ljdp.secure.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.ljdp.secure.cipher.AES;

public class AESDecrypter {
	private String skey;
	private String ivp;

	public AESDecrypter(String skey, String ivp) {
		this.skey = skey;
		this.ivp = ivp;
	}
	
	public String decrypt(String data1) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] debase64 = new Base64(true).decode(data1);
		AES a = new AES(skey, ivp);
		String decryptAes = a.decrypt(debase64);
		return decryptAes;
	}
	
	public String decrypt2(String data1) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] debase64 = Base64.decodeBase64(data1);
		AES a = new AES(skey, ivp);
		String decryptAes = a.decrypt(debase64);
		return decryptAes;
	}
}
