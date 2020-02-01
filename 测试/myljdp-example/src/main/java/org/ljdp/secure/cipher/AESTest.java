package org.ljdp.secure.cipher;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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

import org.apache.commons.codec.binary.Base64;

public class AESTest {
	
	public static void test(String sKey, String ivStr,String msg) throws Exception{
		//构造密钥生成器，指定为AES算法,不区分大小写
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		//生成一个128位的随机源,根据传入的字节数组
		keyGenerator.init(128, new SecureRandom(sKey.getBytes()));
		//产生原始对称密钥
		SecretKey secretKey = keyGenerator.generateKey();
		// key转换,根据字节数组生成AES密钥
		SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
//				skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
		// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		IvParameterSpec ivSpec = new IvParameterSpec(ivStr.getBytes());
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
		//将加密并编码后的内容解码成字节数组
		byte[] result = cipher.doFinal(msg.getBytes());

		System.out.println(Hex.encode(result));
		System.out.println(Base64.encodeBase64String(result));
	}
	

	public static void main(String[] args) {
		String sKey = "1234567890abcdef";
		String ivStr = "1234567890abcdef";
		String msg = "{\"pushSequence\":\"12345689\",\"beginTimestamp\":\"20191231000000\",\"endTImestamp\":\"20191231000000\",\"column\":[\"字段1\",\"字段2\", \"字段3\"],\"data\":[[\"数据1\",\"数据2\",\"数据3\"],[\"数据1\",\"数据2\",\"数据3\"]],\"audit_info\":{\"terminal_info\":\"192.168.1.1\",\"query_timestamp\":\"670985423406\"}}";
		
		try {
			testLjdpAes(sKey, ivStr, msg);
//			test(sKey, ivStr, msg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


	protected static void testLjdpAes(String sKey, String ivStr, String msg)
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		AES a = new AES(sKey, ivStr);
		System.out.println("消息："+msg);
//		String ens = a.encryptString(msg);
//		System.out.println("加密后："+ens);
//		
//		String decs = a.decryptString(ens);
//		System.out.println("解密后："+decs);
		
		System.out.println("======增加Base64======");
		byte[] enbytes = a.encrypt(msg);
		String ens64 = Base64.encodeBase64String(enbytes);
		System.out.println("加密后："+ens64);
		
		byte[] enbytes2 = Base64.decodeBase64(ens64);
		String decs2 = a.decrypt(enbytes2);
		System.out.println("解密后："+decs2);
	}

}
