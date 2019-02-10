package org.ljdp.plugin.batch.view.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.ui.spring.BaseController;
import org.ljdp.util.FileUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet3 文件上传控制器
 * @author hzy
 *
 */
public class FileUploadController extends BaseController {

	/**
	 * 上传文件的处理程序
	 */
	public FileUploadTask getFileUploadTask() {
		try {
			FileUploadTask fileUploadTask = new FileUploadTask(getUser().getId());
			return fileUploadTask;
		} catch (Exception e) {
			FileUploadTask fileUploadTask = new FileUploadTask();
			return fileUploadTask;
		}
	}

	/**
	 * 创建上传文件的任务
	 * @param part  上传的文件（servlet3）
	 * @param rootLocation：设置上传文件存放的根路径，不设置则使用默认配置【batch.fileupload.path】
	 * @param contextPath：文件存放的二级路径，一般用业务有关的命名
	 * @return
	 * @throws IOException
	 */
	private FileUploadTask createFileUploadTask(Part part, String rootLocation, String contextPath) throws IOException {
		FileUploadTask upload = getFileUploadTask();
//		upload.setUploadInputStream(part.getInputStream());
		upload.setUploadPart(part);
		upload.setUploadContentType(part.getContentType());
//		upload.setUploadFileName(MultipartUtil.getFileNameFromPart(part));
		upload.setUploadFileName(FileUtils.getFileName(part.getSubmittedFileName()));
		upload.setContextPath(contextPath);
		upload.setRootLocation(rootLocation);
		if (StringUtils.isBlank(upload.getRootLocation())) {
			ConfigFile cfg = Env.current().getConfigFile();
			String uploadPath = cfg.getValue("batch.fileupload.path");
			if(StringUtils.isBlank(uploadPath)) {
				RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
				HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest(); 
				String defaultPath = request.getServletContext().getRealPath("/upload");
				uploadPath = defaultPath;
			}
			upload.setRootLocation(uploadPath);
		}
		return upload;
	}
	
	/**
	 * 保存文件至本地
	 * @return
	 * @throws IOException 
	 */
	public FileUploadTask saveUploadFile(Part part, String rootLocation, String contextPath) throws IOException {
		FileUploadTask upload = createFileUploadTask(part, rootLocation, contextPath);
		onBeforeUpload();
		upload.run();
		if(upload.getResult().isSuccess()) {
			onUploadSuccess();
		}
		return upload;
	}
	
	protected void onBeforeUpload() {
		
	}
	
	protected void onUploadSuccess() {
		
	}
}
