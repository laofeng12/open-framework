package org.ljdp.secure.cipher;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES加解密算法
 * 此处使用AES-128-CBC加密模式，key需要为16位
 * @author hzy
 *
 */
public class AES {

	private SecretKeySpec skeySpec;
	private IvParameterSpec ivSpec;
	private Charset charset = Charset.forName("utf-8");

	public AES(String sKey, String ivStr) {
		if (sKey == null) {
			throw new UnsupportedOperationException("Key为空null");
		}
		if (ivStr == null) {
			throw new UnsupportedOperationException("iv为空null");
		}
		// 判断是否为16位
		if (sKey.length() != 16) {
			throw new UnsupportedOperationException("Key长度不是16位");
		}
		if (ivStr.length() != 16) {
			throw new UnsupportedOperationException("iv长度不是16位");
		}
		skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		ivSpec = new IvParameterSpec(ivStr.getBytes());
	}

	/**
	 * 加密
	 * @param sMsg 原字符
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws Exception
	 */
	public byte[] encrypt(String sMsg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
		byte[] encrypted = cipher.doFinal(sMsg.getBytes(charset));
		return encrypted;
	}
	
	public String encryptString(String sMsg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		byte[] encrypted = encrypt(sMsg);
		return Hex.encode(encrypted);
	}
	public String encryptBase64(String sMsg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		byte[] encrypted = encrypt(sMsg);
		return Base64.encodeBase64String(encrypted);
	}
	
	/**
	 * 解密
	 * @param encrypted 加密后的字节
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws Exception
	 */
	public String decrypt(byte[] encrypted) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original, charset);    
        return originalString;  
	}
	
	public String decryptString(String encryptedStr) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		byte[] encrypted = Hex.decode(encryptedStr);
		return decrypt(encrypted);
	}
	
	public String decryptBase64(String encryptedStr) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException  {
		byte[] encrypted = Base64.decodeBase64(encryptedStr);
		return decrypt(encrypted);
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}
}
