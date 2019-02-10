package org.ljdp.support.attach.component;

import org.ljdp.component.bean.BaseVO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * FTP配置文件属性
 * @author hzy
 *
 */
@Component
@ConfigurationProperties(prefix = "ljdp.ftpserver")
public class LjdpFtpConfig extends BaseVO{

	private String url;
	private String username;
	private String password;
	private String remotePath;
	private String localTempPath;
	private String mode;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
	public String getLocalTempPath() {
		return localTempPath;
	}
	public void setLocalTempPath(String localTempPath) {
		this.localTempPath = localTempPath;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
