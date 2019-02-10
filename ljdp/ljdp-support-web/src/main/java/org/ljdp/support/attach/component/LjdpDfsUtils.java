package org.ljdp.support.attach.component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ljdp.common.oss.PicUrlUtils;
import org.ljdp.support.attach.domain.BsImageFile;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 分布式文件服务器配置
 * @author hzy
 *
 */
@Component
@ConfigurationProperties(prefix = "ljdp.dfs")
public class LjdpDfsUtils {

	
	private String ftp2httpProxy;//ftp代理服务器
	private String ftp2httpDownload;//ftp远程文件，使用http下载的代理地址
	
	private String ossServerUrl;//阿里云oss服务地址
	private String obsServerUrl;//华为云OBS服务地址
	
	private String vedioServerUrl;//视频服务地址
	
	public String getFtp2httpDownload() {
		return ftp2httpDownload;
	}

	public void setFtp2httpDownload(String ftp2httpDownload) {
		this.ftp2httpDownload = ftp2httpDownload;
	}

	public String getFtp2httpProxy() {
		return ftp2httpProxy;
	}

	public void setFtp2httpProxy(String ftp2httpProxy) {
		this.ftp2httpProxy = ftp2httpProxy;
	}

	public String getOssServerUrl() {
		return ossServerUrl;
	}

	public void setOssServerUrl(String ossServerUrl) {
		this.ossServerUrl = ossServerUrl;
	}

	public String getObsServerUrl() {
		return obsServerUrl;
	}

	public void setObsServerUrl(String obsServerUrl) {
		this.obsServerUrl = obsServerUrl;
	}
	
	public String replaceUrl(String url){
		try{
			if(url == null)return url;
			boolean match = false;
			Pattern p1 = Pattern.compile(PicUrlUtils.OSS_PARAMS2);
			Matcher m1 = p1.matcher(url);
			if(!match) {
				m1 = p1.matcher(url); 
				while(m1.find()){
					if(ossServerUrl != null) {
						url = url.replaceAll(PicUrlUtils.OSS_PARAMS2, ossServerUrl);
						url = url.replace("\\","/");
					}
					match = true;
				}
			}
			
			if(!match) {
				p1 = Pattern.compile(PicUrlUtils.OBS_PARAMS2);
				m1 = p1.matcher(url); 
				while(m1.find()){
					if(obsServerUrl != null) {
						url = url.replaceAll(PicUrlUtils.OBS_PARAMS2, obsServerUrl);
						url = url.replace("\\","/");
					}
					match = true;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return url;
	}
	
	public String replaceVedioUrl(String url){
		try{
			if(url == null)return url;
			boolean match = false;
			Pattern p1 = Pattern.compile(PicUrlUtils.OSS_PARAMS2);
			Matcher m1 = p1.matcher(url);
			if(!match) {
				m1 = p1.matcher(url); 
				while(m1.find()){
					if(ossServerUrl != null) {
						url = url.replaceAll(PicUrlUtils.OSS_PARAMS2, vedioServerUrl);
						url = url.replace("\\","/");
					}
					match = true;
				}
			}
			
			if(!match) {
				p1 = Pattern.compile(PicUrlUtils.OBS_PARAMS2);
				m1 = p1.matcher(url); 
				while(m1.find()){
					if(obsServerUrl != null) {
						url = url.replaceAll(PicUrlUtils.OBS_PARAMS2, vedioServerUrl);
						url = url.replace("\\","/");
					}
					match = true;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return url;
	}
	
	public String replaceUrlToEmpt(String url){
		try{
			if(url == null)return url;
			String value = "";
			boolean match = false;
			Pattern p1 = Pattern.compile(PicUrlUtils.OSS_PARAMS2); 
			Matcher m1 = p1.matcher(url); 
			while(m1.find()){
				url = url.replaceAll(PicUrlUtils.OSS_PARAMS2, value);
				match = true;
			}
			
			if(!match) {
				p1 = Pattern.compile(PicUrlUtils.OBS_PARAMS2); 
				m1 = p1.matcher(url); 
				while(m1.find()){    
				    url = url.replaceAll(PicUrlUtils.OBS_PARAMS2, value);
				    match = true;
				}
			}
			
			if(!match) {
				p1 = Pattern.compile(PicUrlUtils.FTP_PARAMS2); 
				m1 = p1.matcher(url); 
				while(m1.find()){    
				    url = url.replaceAll(PicUrlUtils.FTP_PARAMS2, value);
				    match = true;
				}
			}
			
			if(!match) {
				if(ossServerUrl != null) {
					if(url.startsWith(ossServerUrl)) {
						url = url.replaceFirst(ossServerUrl, "");
						match = true;
					}
				}
			}
			
			if(!match) {
				if(obsServerUrl != null) {
					if(url.startsWith(obsServerUrl)) {
						url = url.replaceFirst(obsServerUrl, "");
						match = true;
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return url;
	}

	public String getViewUrl(BsImageFile f) {
		if(PicUrlUtils.isFtp(f.getPicurl())) {
//			return ftp2httpProxy + f.getId();
			return ftp2httpProxy + replaceUrlToEmpt(f.getPicurl());
		}
		return replaceUrl(f.getPicurl());
	}
	
	public String getDownloadUrl(BsImageFile f) {
		if(PicUrlUtils.isFtp(f.getPicurl())) {
			return ftp2httpDownload + f.getId();
		}
		return replaceUrl(f.getPicurl());
	}
	
	public String getVedioUrl(BsImageFile f) {
//		if(PicUrlUtils.isFtp(f.getPicurl())) {
//			return ftp2httpDownload + f.getId();
//		}
		return replaceVedioUrl(f.getPicurl());
	}

	public String getVedioServerUrl() {
		return vedioServerUrl;
	}

	public void setVedioServerUrl(String vedioServerUrl) {
		this.vedioServerUrl = vedioServerUrl;
	}

}
