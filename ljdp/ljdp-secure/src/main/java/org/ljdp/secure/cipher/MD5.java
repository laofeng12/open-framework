package org.ljdp.secure.cipher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MD5 {

	public static byte[] encode(byte[] byteArray)
			throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] md5Bytes = messageDigest.digest(byteArray);
		return md5Bytes;
	}
	
	public static byte[] encode(String origData, String encode)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return encode(origData.getBytes(encode));
	}
	
	public static byte[] encode(String origData)
			throws NoSuchAlgorithmException {
		return encode(origData.getBytes());
	}
	
	public static String encodeAsString(String origData)
			throws NoSuchAlgorithmException {
		return Hex.encode(encode(origData));
	}
	
	public static String encodeAsString(String origData, String encode)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return Hex.encode(encode(origData, encode));
	}
	
	public static String encodeAsBase64(String data, String charsetName) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return Base64.encodeBase64String(encode(data, charsetName));
	}
	
	public static String encodeFile(File file)
			throws NoSuchAlgorithmException, IOException{
		FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
        messageDigest.update(byteBuffer);  
        byte[] md5Bytes = messageDigest.digest();
        in.close();
        return Hex.encode(md5Bytes);
	}
}
