package org.ljdp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class Md5Util {

	public static String getMd5Pwd(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			char[] charArray = password.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			byte[] md5Bytes = messageDigest.digest(byteArray);
			return toHexString(md5Bytes);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}

	public static String getMd5Pwd(String password, String encode) {
		try {
			byte[] md5Bytes = toMd5(password, encode);
			return toHexString(md5Bytes);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}

	public static String getMd5Pwd_base64(String password) {
		try {
			byte[] md5Bytes = toMd5(password);
			Base64 b64 = new Base64();
			return b64.encodeToString(md5Bytes);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}

	public static String getMd5Pwd_base64(String password, String encode) {
		try {
			byte[] md5Bytes = toMd5(password, encode);
			Base64 b64 = new Base64();
			return b64.encodeToString(md5Bytes);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}
	
	public static String getFileMD5(File file) throws IOException, NoSuchAlgorithmException {
		FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        messageDigest.update(byteBuffer);  
        byte[] md5Bytes = messageDigest.digest();
        in.close();
        return toHexString(md5Bytes);
	}
	public static String getFileMD5_base64(File file) throws IOException, NoSuchAlgorithmException {
		FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        messageDigest.update(byteBuffer);  
        byte[] md5Bytes = messageDigest.digest();
        in.close();
        return Base64.encodeBase64String(md5Bytes);
	}

	protected static String toHexString(byte[] md5Bytes) {
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	protected static byte[] toMd5(String password, String encode)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] byteArray = password.getBytes(encode);
		byte[] md5Bytes = messageDigest.digest(byteArray);
		return md5Bytes;
	}

	protected static byte[] toMd5(String password)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] byteArray = password.getBytes();
		byte[] md5Bytes = messageDigest.digest(byteArray);
		return md5Bytes;
	}
	

}
