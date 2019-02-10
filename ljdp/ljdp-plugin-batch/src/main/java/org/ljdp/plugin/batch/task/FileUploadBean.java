package org.ljdp.plugin.batch.task;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.Part;

import org.ljdp.common.file.nameservice.DateClassifyNameService;
import org.ljdp.component.namespace.FileNameService;
import org.ljdp.component.result.BatchResult;

public class FileUploadBean {
	
	private boolean running = false;
	private boolean complete = false;
	private boolean uploaded = false;
	protected BatchResult result;

	protected File uploadFile;//支持struts上传文件
	protected String uploadContentType;
	protected String uploadFileName;
	protected InputStream uploadInputStream;//支持springmvc,common-upload上传文件
	protected Part uploadPart;//支持Servlet3上传文件
	
	protected String rootLocation;//文件本地存放的根目录
	protected String contextPath;//文件所属业务模块路径
	protected String classifyPath;//文件分类保存路径（例如一般是日期分类的文件夹）
	protected String newFileName;//保存使用的新文件名
	//等于rootLocation+contextPath+classifyPath+newFileName
	protected String saveFileName;// 本地保存的文件全路径名（包含文件名）
	
	protected String fileBusiType;//文件类型（业务性的，并不是指文件格式）

	private FileNameService nameService;
	
	protected String[] supportContentType;
	
	protected String errorFileName;

	public FileUploadBean() {
		super();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public BatchResult getResult() {
		return result;
	}

	public void setResult(BatchResult result) {
		this.result = result;
	}

	/**
	 * saveFileName=rootLocation+contextPath+classifyPath+newFileName
	 * @return
	 */
	public String getSaveFileName() {
		return saveFileName;
	}

	/**
	 * saveFileName=rootLocation+contextPath+classifyPath+newFileName
	 * @param saveFileName
	 */
	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String[] getSupportContentType() {
		return supportContentType;
	}

	public void setSupportContentType(String[] supportContentType) {
		this.supportContentType = supportContentType;
	}

	public FileNameService getNameService() {
		if(null == nameService) {
			nameService = new DateClassifyNameService();
		}
		return nameService;
	}

	public void setNameService(FileNameService nameService) {
		this.nameService = nameService;
	}

	public boolean isUploaded() {
		return uploaded;
	}

	protected void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	public String getErrorFileName() {
		return errorFileName;
	}

	public void setErrorFileName(String errorFileName) {
		this.errorFileName = errorFileName;
	}
	
	public String getRootLocation() {
		return rootLocation;
	}

	public void setRootLocation(String rootLocation) {
		this.rootLocation = rootLocation;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	public String getClassifyPath() {
		return classifyPath;
	}

	public void setClassifyPath(String classifyPath) {
		this.classifyPath = classifyPath;
	}

	public InputStream getUploadInputStream() {
		return uploadInputStream;
	}

	public void setUploadInputStream(InputStream uploadInputStream) {
		this.uploadInputStream = uploadInputStream;
	}

	public Part getUploadPart() {
		return uploadPart;
	}

	public void setUploadPart(Part uploadPart) {
		this.uploadPart = uploadPart;
	}

	public String getFileBusiType() {
		return fileBusiType;
	}

	public void setFileBusiType(String fileBusiType) {
		this.fileBusiType = fileBusiType;
	}

}