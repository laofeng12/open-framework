package org.ljdp.util;

public class TimeUtils {
	
	/**
	 * 根据给出的时间限制判断是最近访问时间是否过期，
	 * limit小于0则无限期，等于0表示过期
	 * @param lastAccessedTime
	 * @param limit
	 * @return
	 */
	public static boolean isOverdueSecond(long lastAccessedTime, long secondLimit) {
		if(secondLimit == 0) {
			return true;
		}
		if(secondLimit < 0) {
			return false;
		}
		long interval = System.currentTimeMillis() - lastAccessedTime;
		long interval_sec = interval / 1000;
//		System.out.println("使用时间＝"+interval_sec);
		if(interval_sec > secondLimit) {
			return true;
		}
		return false;
	}
}
