package org.ljdp.plugin.batch.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.json.JSONTools;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.plugin.batch.check.CheckFormat;
import org.ljdp.plugin.batch.model.BatchUploadFileResult;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.ui.struts2.BaseAction;

public class FileUploadAction extends BaseAction {
	private static final long serialVersionUID = -5091161508589451279L;
	
	private FileUploadTask fileUploadTask;
	private FileUploadTask attachUploadTask;
	private String uploadID;
	private String json;
	
	protected File uploadFile;
	protected String uploadFileContentType;
	protected String uploadFileFileName;
	protected String uploadName = "UPLOAD";
	
	protected File[] attachFiles;
	protected String[] attachFilesContentType;
	protected String[] attachFilesFileName;
	
	protected String checkClass;
	protected Integer checkBeginRow = 2;//默认第一行为标题，从第二行开始检查

	public FileUploadAction() {
		super();
	}
	
	public FileUploadAction(String uploadName) {
		super();
		this.uploadName = uploadName;
	}

	protected String getUploadTaskName(String id) {
		return uploadName + "_" + id;
	}

	public String getUploadName() {
		return uploadName;
	}

	public void setUploadName(String uploadName) {
		this.uploadName = uploadName;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileContentType() {
		return uploadFileContentType;
	}

	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public String getUploadID() {
		return uploadID;
	}

	public void setUploadID(String uploadID) {
		this.uploadID = uploadID;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	/**
	 * 上传文件的处理程序
	 */
	public FileUploadTask getFileUploadTask() {
		if(fileUploadTask == null) {
			fileUploadTask = new FileUploadTask(getUser().getId());
			if(StringUtils.isNotBlank(checkClass)) {
				try {
					Class<?> cls = Class.forName(checkClass);
					Object checkObj = cls.newInstance();
					if(checkObj instanceof CheckFormat) {
						CheckFormat c = (CheckFormat)checkObj;
						c.setBeginIndex(checkBeginRow);
						fileUploadTask.setICheckFormat(c);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return fileUploadTask;
	}

	public void setFileUploadTask(FileUploadTask fileUploadTask) {
		this.fileUploadTask = fileUploadTask;
	}

	private FileUploadTask createFileUploadTask() {
		FileUploadTask upload = getFileUploadTask();
		upload.setUploadFile(getUploadFile());
		upload.setUploadContentType(getUploadFileContentType());
		upload.setUploadFileName(getUploadFileFileName());
		if (StringUtils.isBlank(upload.getRootLocation())) {
			ConfigFile cfg = Env.current().getConfigFile();
			String defaultPath = ServletActionContext.getServletContext().getRealPath("/upload");
			String uploadPath = cfg.getValue("batch.fileupload.path", defaultPath);
			upload.setRootLocation(uploadPath);
		}
		return upload;
	}

	/**
	 * 批量附件上传任务
	 * @return
	 */
	public FileUploadTask getAttachUploadTask() {
		if(attachUploadTask == null) {
			attachUploadTask = new FileUploadTask(getUser().getId());
		}
		return attachUploadTask;
	}
	
	public void resetBatchTask() {
		attachUploadTask = null;
	}

	private FileUploadTask createAttachUploadTask(int i) {
		FileUploadTask upload = getAttachUploadTask();
		upload.setUploadFile(attachFiles[i]);
		upload.setUploadContentType(attachFilesContentType[i]);
		upload.setUploadFileName(attachFilesFileName[i]);
		if (StringUtils.isBlank(upload.getRootLocation())) {
			ConfigFile cfg = Env.current().getConfigFile();
			String defaultPath = ServletActionContext.getServletContext().getRealPath("/upload");
			String uploadPath = cfg.getValue("batch.fileupload.path", defaultPath);
			upload.setRootLocation(uploadPath);
		}
		return upload;
	}

	/**
	 * 线程模式上传文件。
	 * 建立一线程上传文件，并马上返回，页面需要调用uploadState()检查上传情况和状态
	 * 比较适合较大文件上传并且上传时需要对文件进行检查
	 * @return
	 */
	public String uploadFile() {
		// System.out.println(uploadFileFileName + " "+ uploadFileContentType);
		getSession().removeAttribute(getUploadTaskName(uploadID));
		FileUploadTask upload = createFileUploadTask();
		getSession().setAttribute(getUploadTaskName(getUploadID()), upload);
		getRequest().setAttribute("uploadJustnow", true);
		BatchResult res = new GeneralBatchResult();
		res.setSuccess(true);
		json = JSONTools.toJSON(res, "yyyy-MM-dd");
		onBeforeUpload();
		Thread uploadThread = new Thread(upload);
		uploadThread.start();
		return SUCCESS;
	}

	/**
	 * 上传文件
	 */
	public void uploadFileNow() {
		if(uploadFile != null) {
			FileUploadTask upload = saveUploadFile();
			JSONTools.writeHTML(upload.getResult(), getResponse());
		} else {
			Result res = new GeneralResult();
			res.setSuccess(false);
			res.setMsg("上传失败");
			JSONTools.writeHTML(res, getResponse());
		}
	}

	/**
	 * 保存文件至本地
	 * @return
	 */
	protected FileUploadTask saveUploadFile() {
		FileUploadTask upload = createFileUploadTask();
		onBeforeUpload();
		upload.run();
		if(upload.getResult().isSuccess()) {
			onUploadSuccess();
		}
		return upload;
	}
	
	public void batchUploadNow() {
		BatchUploadFileResult result = saveAttachFiles();
		result.setData(null);
		JSONTools.writeHTML(result, getResponse());
	}
	
	/**
	 * 保存批量上传的附件到本地
	 * @return
	 */
	protected BatchUploadFileResult saveAttachFiles() {
		BatchUploadFileResult result = new BatchUploadFileResult();
		result.setSuccess(true);
		List<FileUploadTask> succList = new ArrayList<FileUploadTask>();
		result.setData(succList);
		if(attachFiles == null || attachFilesContentType == null || attachFilesFileName == null) {
			return result;
		}
		int ok = 0;
		int fail = 0;
		int total = 0;
		String msg = "";
		for (int i = 0; i < attachFiles.length; i++) {
			if(StringUtils.isBlank(attachFilesFileName[i]) || attachFiles[i] == null) {
				continue;
			}
			total++;
			resetBatchTask();
			FileUploadTask upload = createAttachUploadTask(i);
			onBeforeSaveAttach(upload);
			upload.run();
			if(upload.getResult().isSuccess()) {
				ok++;
				succList.add(upload);
				onSaveAttachSuccess(upload);
			} else {
				fail++;
				msg += attachFilesFileName[i]+"："+upload.getResult().getMsg()+"<br>";
			}
		}
		if(total == 0) {
			return result;
		}
		if(ok == 0) {
			result.setSuccess(false);
		}
		result.setMsg(msg);
		result.setOk(ok);
		result.setFail(fail);
		result.setTotalCount(total);
		return result;
	}

	/**
	 * 返回线程模式上传文件的状态
	 * 
	 * @throws Exception
	 */
	public void uploadState() throws Exception {
		FileUploadTask task = 
			(FileUploadTask)getSession().getAttribute(getUploadTaskName(uploadID));
		if(task != null) {
			if( task.isComplete() ) {
				getSession().removeAttribute(getUploadTaskName(uploadID));
			}
		}
		JSONTools.writePage(task, getResponse());
	}
	
	protected void onBeforeUpload() {
		
	}
	
	protected void onUploadSuccess() {
		
	}
	
	protected void onBeforeSaveAttach(FileUploadTask upload) {
		
	}
	
	protected void onSaveAttachSuccess(FileUploadTask upload) {
		
	}

	public File[] getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(File[] attachFiles) {
		this.attachFiles = attachFiles;
	}

	public String[] getAttachFilesContentType() {
		return attachFilesContentType;
	}

	public void setAttachFilesContentType(String[] attachFilesContentType) {
		this.attachFilesContentType = attachFilesContentType;
	}

	public String[] getAttachFilesFileName() {
		return attachFilesFileName;
	}

	public void setAttachFilesFileName(String[] attachFilesFileName) {
		this.attachFilesFileName = attachFilesFileName;
	}

	public String getCheckClass() {
		return checkClass;
	}

	/**
	 * 设置检查文件的类名，此类必需实现接口org.opensource.ljdp.plugin.batch.check.CheckFormat
	 * @param checkClass
	 */
	public void setCheckClass(String checkClass) {
		this.checkClass = checkClass;
	}

	public Integer getCheckBeginRow() {
		return checkBeginRow;
	}

	/**
	 * 设置从第?行开始检查文件
	 * @param checkBeginRow
	 */
	public void setCheckBeginRow(Integer checkBeginRow) {
		this.checkBeginRow = checkBeginRow;
	}

}
