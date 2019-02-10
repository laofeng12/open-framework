package org.ljdp.common.secure;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;

public class DES {
	private Key key;
	
	public DES(String keyWord) throws NoSuchAlgorithmException {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		Security.addProvider(new com.ibm.crypto.provider.IBMJCE());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keyWord.getBytes());
		keyGenerator.init(56, secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		key = new SecretKeySpec(secretKey.getEncoded(), "DES");
	}
	
	/**
	 * 加密
	 * @param byteS
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public byte[] encrypt(byte[] byteS) throws NoSuchAlgorithmException,
							NoSuchPaddingException,
							InvalidKeyException,
							IllegalBlockSizeException,
							BadPaddingException {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] byteFina = cipher.doFinal(byteS);
		return byteFina;
	}
	
	/**
	 * 解密
	 * @param byteD
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public byte[] decrypt(byte[] byteD) throws NoSuchAlgorithmException,
							NoSuchPaddingException,
							InvalidKeyException,
							IllegalBlockSizeException,
							BadPaddingException {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] byteFina = cipher.doFinal(byteD);
		return byteFina;
	}
	
	/**
	 * 加密
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String encrypt(String value) throws NoSuchAlgorithmException,
									NoSuchPaddingException,
									InvalidKeyException,
									IllegalBlockSizeException,
									BadPaddingException,
									UnsupportedEncodingException {
		Base64 base64 = new Base64();
		byte[] byteMi = encrypt(value.getBytes("UTF-8"));
		String encrypt = new String(base64.encode(byteMi), "UTF-8");
		return encrypt;
	}
	
	/**
	 * 解密
	 * @param originalString
	 * @return
	 * @throws DecoderException 
	 * @throws UnsupportedEncodingException 
	 */
	public String decrypt(String originalString) throws NoSuchAlgorithmException,
									NoSuchPaddingException,
									InvalidKeyException,
									IllegalBlockSizeException,
									BadPaddingException,
									DecoderException,
									UnsupportedEncodingException {
		Base64 base64 = new Base64();
		byte[] byteMi = base64.decode(originalString.getBytes("UTF-8"));
		byte[] byteMing = decrypt(byteMi);
		String decrypt = new String(byteMing, "UTF-8");
		return decrypt;
	}

}
