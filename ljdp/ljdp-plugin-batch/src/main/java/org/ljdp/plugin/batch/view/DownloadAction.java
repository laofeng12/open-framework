package org.ljdp.plugin.batch.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.ljdp.common.file.ContentType;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.ui.struts2.BaseAction;
import org.ljdp.util.FileUtils;

/**
 * 通用文件下载。非安全，可以访问任意文件，建议内部使用！
 * @author hzy
 *
 */
public class DownloadAction extends BaseAction {

	private static final long serialVersionUID = -7053321938443548758L;
	
	public static final String[] NOTACCESS_PATH = {"/etc/"};//安全目录，不允许访问
	
	private String downloadFileName;//下载时客户端显示的文件名
	private String fullFilePath;//文件存放的完整路径

	/**
	 * 下载文件
	 */
	public void downloadFile() {
		System.out.println("downname="+downloadFileName);
		HttpServletResponse response = getResponse();
		if (StringUtils.isNotBlank(downloadFileName)) {
			//防止往上层目录检索
			downloadFileName = downloadFileName.replaceAll("\\.\\.", "");
			while(downloadFileName.indexOf("//") >= 0) {
				downloadFileName = downloadFileName.replaceAll("//", "/");
			}
			
			//如果是纯文件名称没有给出目录，则使用默认目录
			if (downloadFileName.indexOf("\\") == -1
					&& downloadFileName.indexOf("/") == -1
					&& StringUtils.isBlank(fullFilePath)) {
				String location = ServletActionContext.getServletContext()
						.getRealPath("/download");
				String downfile = location + "/" + downloadFileName;
				fullFilePath = downfile;
			} else {
				if(StringUtils.isBlank(fullFilePath)) {
					fullFilePath = downloadFileName;
				}
			}
		} else if (StringUtils.isNotBlank(fullFilePath)) {
			//防止往上层目录检索
			fullFilePath = fullFilePath.replaceAll("\\.\\.", "");
			while(fullFilePath.indexOf("//") >= 0) {
				fullFilePath = fullFilePath.replaceAll("//", "/");
			}
		}
		System.out.println("fullpath="+fullFilePath);
		try {
			//下载前事件，通常用来设置downloadFileName和fullFilePath
			onBeforeDownload();
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure(e.getMessage(), getResponse());
			return;
		}
		//简单安全检查，防止进入系统目录
		for(int i = 0; i < NOTACCESS_PATH.length; i++) {
			if(fullFilePath.startsWith(NOTACCESS_PATH[i])) {
				ExtUtils.writeHtmlFailure("拒绝访问系统路径", getResponse());
				return;
			}
		}
		
		String name;
		if (downloadFileName != null) {
			name = FileUtils.getFileName(downloadFileName);
		} else {
			name = FileUtils.getFileName(fullFilePath);
		}
		InputStream input = null;
		OutputStream os = null;
		try {
			File file = new File(fullFilePath);
			if(!file.exists()) {
				throw new Exception("文件不存在");
			}
			input = new BufferedInputStream(new FileInputStream(file));
			response.reset();
			try {
				String contenttype = ContentType.getContentType(name);
				response.setContentType(contenttype);
			} catch(UnsupportedOperationException e) {
			}
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(name.getBytes("GBK"), "iso-8859-1"));
			os = response.getOutputStream();
			FileUtils.copy(input, os);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure(e.getMessage(), getResponse());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if(os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void onBeforeDownload() throws Exception{
	}

	/**
	 * 下载时客户端显示的文件名
	 */
	public String getDownloadFileName() {
		return downloadFileName;
	}

	/**
	 * 下载时客户端显示的文件名
	 */
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	/**
	 * 待下载文件存放的完整路径
	 */
	public String getFullFilePath() {
		return fullFilePath;
	}

	/**
	 * 待下载文件存放的完整路径
	 */
	public void setFullFilePath(String fullFilePath) {
		this.fullFilePath = fullFilePath;
	}

}
