package org.ljdp.common.secure;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AES {
	
	private Key key;
	private SecretKeySpec skeySpec;
	private IvParameterSpec iv;

	/**
	 * AES加密算法
	 * @throws NoSuchAlgorithmException 
	 */
	public AES(String keyWord) throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keyWord.getBytes());
		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		key = new SecretKeySpec(secretKey.getEncoded(), "AES");
	}

	public AES(String sKey, String ivParameter) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] raw = sKey.getBytes();
        skeySpec = new SecretKeySpec(raw, "AES");
        iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
	}

	/**
	 * 加密
	 * @throws InvalidAlgorithmParameterException 
	 */
	public byte[] encrypt(byte[] content) throws NoSuchAlgorithmException, 
							NoSuchPaddingException, 
							UnsupportedEncodingException, 
							InvalidKeyException, 
							IllegalBlockSizeException, 
							BadPaddingException, InvalidAlgorithmParameterException {
		if(key != null) {
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} else {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] result = cipher.doFinal(content);
			return result;
		}
	}

	/**
	 * 加密并把结果转换为十六进制
	 * @throws InvalidAlgorithmParameterException 
	 */
	public String encrypt(String content) throws NoSuchAlgorithmException, 
								NoSuchPaddingException, 
								UnsupportedEncodingException, 
								InvalidKeyException, 
								IllegalBlockSizeException, 
								BadPaddingException, InvalidAlgorithmParameterException {
		return Hex.encodeHexString(encrypt(content.getBytes("UTF-8")));
	}
	/**
	 * 加密并把结果转换为base64
	 */
	public String encryptBase64(String content) throws NoSuchAlgorithmException, 
								NoSuchPaddingException, 
								UnsupportedEncodingException, 
								InvalidKeyException, 
								IllegalBlockSizeException, 
								BadPaddingException, InvalidAlgorithmParameterException {
		return Base64.encodeBase64String(encrypt(content.getBytes("UTF-8")));
	}

	/**
	 * 解密
	 * @throws InvalidAlgorithmParameterException 
	 */
	public byte[] decrypt(byte[] content) throws NoSuchAlgorithmException,
								NoSuchPaddingException,
								InvalidKeyException,
								IllegalBlockSizeException,
								BadPaddingException, InvalidAlgorithmParameterException {
		if(key != null) {
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 解密
		} else {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] result = cipher.doFinal(content);
			return result; // 解密
		}
	}

	/**
	 * 解密来自十六进制的数据
	 * @throws InvalidAlgorithmParameterException 
	 */
	public String decrypt(String content)  throws NoSuchAlgorithmException,
									NoSuchPaddingException,
									InvalidKeyException,
									IllegalBlockSizeException,
									BadPaddingException,
									DecoderException,
									UnsupportedEncodingException, InvalidAlgorithmParameterException {
		byte[] decryptByts = decrypt(Hex.decodeHex(content.toCharArray()));
		String result = new String(decryptByts, "UTF-8");
		return result;
	}
	/**
	 * 解密来自base64的数据
	 */
	public String decryptBase64(String content)  throws NoSuchAlgorithmException,
									NoSuchPaddingException,
									InvalidKeyException,
									IllegalBlockSizeException,
									BadPaddingException,
									DecoderException,
									UnsupportedEncodingException, InvalidAlgorithmParameterException {
		byte[] decryptByts = decrypt(Base64.decodeBase64(content));
		String result = new String(decryptByts, "UTF-8");
		return result;
	}
}
