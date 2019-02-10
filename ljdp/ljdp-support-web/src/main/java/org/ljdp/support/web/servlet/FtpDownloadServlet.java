package org.ljdp.support.web.servlet;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.ftp.ApacheFTPClient;
import org.ljdp.plugin.batch.view.spring.DownloadController;
import org.ljdp.support.attach.component.LjdpFtpConfig;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.util.FileUtils;

@WebServlet(urlPatterns="/ftp/*")
public class FtpDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = -6539311880054817325L;
	
	@Resource
	private LjdpFtpConfig config;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String context = request.getContextPath()+"/ftp";
		String remotePath = request.getRequestURI().replaceFirst(context, "");
		
		String filename = FileUtils.getFileName(remotePath);
		
		String localTempPath = FileUtils.joinDirectory(config.getLocalTempPath(), filename);
		File localTempFile = new File(localTempPath); 
		try {
			if(!localTempFile.exists()) {
				//文件不存在，去ftp下载
				ApacheFTPClient ftpclient = new ApacheFTPClient(config.getUrl(), config.getUsername(), config.getPassword(), config.getMode());
				
				boolean res = ftpclient.downloadFile(remotePath, localTempPath);
				System.out.println("[FTP]download:"+res+"\n"+
						"remote="+remotePath+"\n"+
						"local="+localTempPath);
				if(!res) {
					ExtUtils.writeHtmlFailure("文件已丢失", response);
					return;
				}
			}
			DownloadController downloadCtl = new DownloadController();
			downloadCtl.showFile(request, response, 
					localTempPath	//文件在本地服务器存放路径
					);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure("连接文件服务器失败", response);
		}
	}

}
