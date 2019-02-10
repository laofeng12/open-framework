package org.ljdp.common.oss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 图片地址处理
 * @author liaojinhua
 *
 */
public class PicUrlUtils {
	public final static String OSS_PARAMS = "${oss_url}";
	public final static String OSS_PARAMS2 = "\\$\\{oss_url\\}";
	public final static String OBS_PARAMS = "${obs_url}";
	public final static String OBS_PARAMS2 = "\\$\\{obs_url\\}";
	public final static String FTP_PARAMS = "${ftp_url}";
	public final static String FTP_PARAMS2 = "\\$\\{ftp_url\\}";
	
	public static boolean isFtp(String url) {
		Pattern p1 = Pattern.compile(FTP_PARAMS2); 
		Matcher m1 = p1.matcher(url);
		while(m1.find()){
			return true;
		}
		return false;
	}
	
}
