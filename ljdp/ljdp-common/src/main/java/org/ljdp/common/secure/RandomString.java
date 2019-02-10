package org.ljdp.common.secure;

import java.util.Random;

/**
 * 生成随机码
 * @author hzy
 *
 */
public class RandomString {

	/**
	 * 生成一个随机数字码
	 * @param codelen
	 * @return
	 */
	public static String generalNumCode(int codelen) {
		Random random = new Random();
		String authCode = "";
		for(int i = 0; i < codelen; ++i) {
			authCode += random.nextInt(10);
		}
		return authCode;
	}
	
}
