package org.ljdp.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

/**
 * 生成一个随机的编码
 * 
 * @author hzy
 *
 */
public class RandomCode {

	private static SecureRandom sr;
	private static Random random = new Random();
	static {
		try {
			sr = SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			sr = new SecureRandom();
		}
	}

	/**
	 * 获取12位长度的随机数字字符串
	 * 
	 * @return
	 */
	public static String getNumberStrOf12() {
		return getNumberStrOf4_BackPad() + getNumberStrOf4_FontPad() + getNumberStrOf4_FontPad();
	}

	/**
	 * 获取4位随机数，不到4位前面补0
	 * 
	 * @return
	 */
	public static String getNumberStrOf4_FontPad() {
		int n = sr.nextInt(9999);
		if (n <= 0) {
			n = 0;
		}
		StringBuilder sb = new StringBuilder(4);
		if (n < 10) {
			sb.append("000").append(n);
		} else if (n < 100) {
			sb.append("00").append(n);
		} else if (n < 1000) {
			sb.append("0").append(n);
		} else {
			sb.append(n);
		}

		return sb.toString();
	}

	/**
	 * 获取4位随机数，不到4位后面补0
	 * 
	 * @return
	 */
	public static String getNumberStrOf4_BackPad() {
		int n = sr.nextInt(9999);
		if (n <= 0) {
			n = 0;
		}
		StringBuilder sb = new StringBuilder(4);
		if (n < 10) {
			sb.append(n).append("000");
		} else if (n < 100) {
			sb.append(n).append("00");
		} else if (n < 1000) {
			sb.append(n).append("0");
		} else {
			sb.append(n);
		}

		return sb.toString();
	}

	/**
	 * 获取12位随机长整形数字
	 * 
	 * @return
	 */
	public static long getNumberOf12() {
		int a = getNumberOf4();
		int b = getNumberOf4();
		int c = getNumberOf4();
		return a * 100000000L + b * 10000L + c;
	}

	/**
	 * 获取4位随机数字
	 * 
	 * @return
	 */
	public static int getNumberOf4() {
		int n = sr.nextInt(9999);
		if (n <= 0) {
			n = 1;
		}
		if (n < 10) {
			n *= 1000;
		} else if (n < 100) {
			n *= 100;
		} else if (n < 1000) {
			n *= 10;
		}
		return n;
	}

	/**
	 * 生成一个16位随机字符串
	 * 
	 * @return
	 */
	public static String getString16() {
		byte[] randomBytes = new byte[12];
		sr.nextBytes(randomBytes);
		String s = Base64.encodeBase64URLSafeString(randomBytes);
		return s;
	}

	/**
     * 生成随机验证码
     * @param type  类型
     * @param length   长度
     * @param exChars  排除的字符
     * @return
     */
    public static String getCode(int length,String exChars){
    	int i = 0;
    	StringBuilder sb = new StringBuilder(length);
    	while(i<length){
    	    int t=random.nextInt(123);
    	    if((t>=97||(t>=65&&t<=90)||(t>=48&&t<=57))&&(exChars==null||exChars.indexOf((char)t)<0)){
    	        sb.append((char)t);
    	        i++;
    	    }
    	}
    	return sb.toString();
    }
	
//	public static void main(String[] args) {
//		for(int i=0;i<10;i++) {
//			String s = getNumberStrOf12();
//			if(s.length() < 12) {
//				System.out.println(s);
//				break;
//			}
//			System.out.println(s);
//		}
//		System.out.println("==================");
//		for(int i=0;i<10;i++) {
//			String s = getRandomCode(4, null);
//			System.out.println(s);
//		}
//	}

}
