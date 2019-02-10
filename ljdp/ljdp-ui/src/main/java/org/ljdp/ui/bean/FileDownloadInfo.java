package org.ljdp.ui.bean;

/**
 * 文件下载信息
 * @author hzy
 *
 */
public class FileDownloadInfo {
	private String path;
	private String clientName;
	
	public FileDownloadInfo(String path, String clientName) {
		super();
		this.path = path;
		this.clientName = clientName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
