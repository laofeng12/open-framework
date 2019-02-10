package org.ljdp.plugin.batch.task;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.DynamicField;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.TxtXlsResultFile;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.plugin.batch.check.CheckFormat;
import org.ljdp.plugin.batch.check.FormatException;
import org.ljdp.plugin.batch.util.FileChecker;
import org.ljdp.util.FileUtils;

public class FileUploadTask extends FileUploadBean implements Runnable {
	protected String operCode = "visitor";
	protected CheckFormat iCheckFormat;
	protected ArrayList<DynamicField> checkFields;
	protected String operType;
	
	public FileUploadTask() {
		super();
	}

	public FileUploadTask(String operCode) {
		super();
		this.operCode = operCode;
	}

	public FileUploadTask(String operCode, CheckFormat checkFormat) {
		super();
		this.operCode = operCode;
		iCheckFormat = checkFormat;
	}

	public FileUploadTask(String operCode, CheckFormat checkFormat,
			ArrayList<DynamicField> checkFields) {
		super();
		this.operCode = operCode;
		iCheckFormat = checkFormat;
		this.checkFields = checkFields;
	}

	public void run() {
		setRunning(true);
		try {
			result = new GeneralBatchResult();
			File localFile = uploadFile();
			setUploaded(true);
			TxtXlsResultFile res = checkFile(localFile);
			if(res != null) {
				if(res.getErrorCount() > 0) {
					setErrorFileName(res.getErrResultFileName());
					result.setErrorFileName(getErrorFileName());
					result.setFail(res.getErrorCount());
					result.setMsg("文件数据检查不通过,请查看错误文件");
//					throw new FormatException("文件数据检查不通过,请查看错误文件");
				}
				result.setOk(getPassCount());
			}
			result.setSuccess(true);
			result.setInputFileName(getUploadFileName());//上传的文件名
			result.setOutputFileName(getSaveFileName());//保存成功后的完整路径+文件名
		}catch(Throwable e) {
//			while(e.getCause() != null) {
//				e = e.getCause();
//			}
			e.printStackTrace();
			FileUtils.delete(
					FileUtils.perfectDirectory(rootLocation) + saveFileName);
			result.setSuccess(false);
			if(StringUtils.isNotBlank(e.getMessage())) {				
				result.setMsg(e.getMessage());
			} else {
				result.setMsg(e.toString());
//				e.printStackTrace();
			}
		} finally {
			setComplete(true);
			setRunning(false);
		}
	}

	protected TxtXlsResultFile checkFile(File localFile) throws Exception, FormatException {
//		int count = -1;
		if(iCheckFormat == null) {
			return null;
		}
		setCheckFields();
		if(getUploadContentType().equals(ContentType.TEXT)) {
			return FileChecker.checkTxtFile(localFile, iCheckFormat, checkFields);				
		} else if(getUploadContentType().equals(ContentType.EXCEL)
				|| uploadFileName.endsWith(".xls")) {
			 return FileChecker.checkExcelFileAuto(localFile, iCheckFormat, checkFields);
		}
		return null;
	}

	protected File uploadFile() throws Exception {
		if (getSupportContentType() != null
				&& getSupportContentType().length > 0
				&& uploadContentType != null) {
			int i;
			for (i = 0; i < getSupportContentType().length; ++i) {
				if (uploadContentType.equals(getSupportContentType()[i])) {
					if(uploadContentType.equals(ContentType.STREAM)) {
						if(getUploadFileName().endsWith(".xls") ||
								getUploadFileName().endsWith(".xlsx")) {
							break;
						}
					} else {
						break;
					}
				}
			}
			if (getSupportContentType().length == i) {
				String msg = "";
				for (String type : getSupportContentType()) {
					String typename = ContentType.getContentTypeName(type);
					msg += typename + "、";
				}
				throw new Exception("导入的文件类型不正确,此导入支持类型有：" + msg);
			}
		}
//		if (uploadFile == null) {
//			throw new Exception("上传的文件不存在");
//		}
		String classifyPathFileName = getNameService().createFileName(uploadFileName, operCode);
		String fullPathFileName = FileUtils.joinDirectory(
				new String[] {rootLocation, contextPath, classifyPathFileName});
		System.out.println("localfullPathFileName="+fullPathFileName);
		String[] pathAndName = FileUtils.getPathAndName(classifyPathFileName);
		//分类路径
		if(getClassifyPath() == null) {
			setClassifyPath(pathAndName[0]);
		} else {
			setClassifyPath(
					FileUtils.joinDirectory(getClassifyPath(), pathAndName[0]));
		}
		//新文件名
		setNewFileName(pathAndName[1]);
		//全路径
		setSaveFileName(fullPathFileName);
		
		File localDir = new File(FileUtils.getDirectory(fullPathFileName));
		localDir.mkdirs();
		File localFile = new File(fullPathFileName);
		boolean res;
		if(uploadFile != null) {
			res = FileUtils.copy(uploadFile, localFile);
		} else if(uploadInputStream != null){
			try {
				OutputStream outs = new BufferedOutputStream(new FileOutputStream(localFile));
				FileUtils.copy(uploadInputStream, outs);
//				uploadInputStream.close();
				outs.flush();
				outs.close();
				res = true;
			} catch (IOException e) {
				res = false;
				e.printStackTrace();
			}
		} else if(uploadPart != null) {
			uploadPart.write(fullPathFileName);
			res = true;
		} else {
			throw new Exception("未设置要上传的文件");
		}
		if(!res) {
			throw new Exception("上传文件失败,请再次尝试");
		}
		return localFile;
	}
	
	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public CheckFormat getICheckFormat() {
		return iCheckFormat;
	}

	public void setICheckFormat(CheckFormat checkFormat) {
		iCheckFormat = checkFormat;
	}

	protected ArrayList<DynamicField> getCheckFields() {
		return checkFields;
	}

	public void setCheckFields(ArrayList<DynamicField> checkFields) {
		this.checkFields = checkFields;
	}
	
	public void setCheckFields() {
		
	}
	
	public int getPassCount() {
		if(iCheckFormat == null) {
			return 0;
		}
		return iCheckFormat.getPassCount();
	}
	
	public int getCurrentRow() {
		if(iCheckFormat == null) {
			return 0;
		}
		return iCheckFormat.getCurrentRow();
	}
	
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

}
