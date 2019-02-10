package org.ljdp.support.web.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.ftp.ApacheFTPClient;
import org.ljdp.plugin.batch.view.spring.DownloadController;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.component.LjdpFtpConfig;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.service.FtpImageFileService;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.util.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ljdp/dfs")
public class FileDownloadAction {
	
	@Resource
	private FtpImageFileService ftpImageFileService;
	@Resource
	private LjdpFtpConfig config;
	@Resource
	private LjdpDfsUtils dfsUtils;

	@RequestMapping(value="/downloadFile.act")
	public void downloadFile(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("fullFilePath")String fullFilePath) {
		String filename = FileUtils.getFileName(fullFilePath);
		
		DownloadController downloadCtl = new DownloadController();
		downloadCtl.downloadFile(request, response, 
				filename, //客户端显示的文件名
				fullFilePath	//文件在本地服务器存放路径
				);
	}
	
	@RequestMapping(value="/downloadByFid.act")
	public void downloadByFid(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("fid")Long fid) {
		BsImageFile f = ftpImageFileService.get(fid);
		if(f == null) {
			ExtUtils.writeHtmlFailure("文件记录不存在", response);
			return;
		}
		String remotePath = dfsUtils.replaceUrlToEmpt(f.getPicurl());
		String filename = FileUtils.getFileName(remotePath);
		String showFilename = f.getPicname();
		
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
			downloadCtl.downloadFile(request, response, 
					showFilename, //客户端显示的文件名
					localTempPath	//文件在本地服务器存放路径
					);
		} catch (Exception e) {
			e.printStackTrace();
			ExtUtils.writeHtmlFailure("连接文件服务器失败", response);
		}
	}
	
	@RequestMapping(value="/{fid}")
	public void showFileFromDfs(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fid")String fid) {
		BsImageFile f = ftpImageFileService.get(new Long(fid));
		if(f == null) {
			ExtUtils.writeHtmlFailure("文件记录不存在", response);
			return;
		}
		String remotePath = dfsUtils.replaceUrlToEmpt(f.getPicurl());
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
