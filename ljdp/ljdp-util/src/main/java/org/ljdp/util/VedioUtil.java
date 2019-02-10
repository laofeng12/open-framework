package org.ljdp.util;

import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

public class VedioUtil {

	/**
	 * 获取视频时长（毫秒）
	 * @param source
	 * @return
	 */
	public static long readVideoTime(File source) {
		Encoder encoder = new Encoder();
		try {
			MultimediaInfo m = encoder.getInfo(source);
			return m.getDuration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
//	public static void main(String[] args) {
//		File f = new File("E:\\导读/test123.mp4");
//		System.out.println(readVideoTime(f));
//	}
}
