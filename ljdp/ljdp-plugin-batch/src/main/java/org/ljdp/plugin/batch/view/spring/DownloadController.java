package org.ljdp.plugin.batch.view.spring;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.util.FileUtils;

/**
 * 文件下载控制器，为了使用简单安全性一般，建议内网使用
 * @author hzy
 *
 */
public class DownloadController {
	public static final String[] NOTACCESS_PATH = {"/etc/"};//安全目录，不允许访问
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param downloadFileName  下载时客户端显示的文件名
	 * @param fullFilePath      文件存放的完整路径
	 */
	public void downloadFile(HttpServletRequest request, 
			HttpServletResponse response,
			String downloadFileName,
			String fullFilePath) {
		System.out.println("downname="+downloadFileName);
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
				String location = request.getServletContext()
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

		//简单安全检查，防止进入系统目录
		for(int i = 0; i < NOTACCESS_PATH.length; i++) {
			if(fullFilePath.startsWith(NOTACCESS_PATH[i])) {
				ExtUtils.writeHtmlFailure("拒绝访问系统路径", response);
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
			OutputStream os = response.getOutputStream();
			FileUtils.copy(input, os);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure(e.getMessage(), response);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void showFile(HttpServletRequest request, HttpServletResponse response,
			String fullFilePath) {
		String name = FileUtils.getFileName(fullFilePath);
		InputStream input = null;
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
				if(contenttype.equals(ContentType.MP4)) {
					long filesize = new Double(FileUtils.getFileSizeOfByte(file)).longValue();
					response.addHeader("Content-Length", String.valueOf(filesize));
					response.addHeader("Accept-Ranges", "bytes");
				}
			} catch(UnsupportedOperationException e) {
				e.printStackTrace();
			}
			
			OutputStream os = response.getOutputStream();
			FileUtils.copy(input, os);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure(e.getMessage(), response);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
