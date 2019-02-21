package org.ljdp.support.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.plugin.batch.view.spring.FileUploadController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.service.FtpImageFileService;
import org.ljdp.support.attach.service.HwObsService;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.support.attach.vo.ImageResult;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.ui.extjs.ExtUtils;
import org.ljdp.util.ByteUtil;
import org.ljdp.util.FileUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 文件上传，附件上传管理
 * 
 * @author hzy
 *
 */
@Controller
@RequestMapping("/ljdp/attach")
public class LjdpUploadController extends FileUploadController {
	private String rootLocation;
	@Resource
	private LjdpFileuploadConfig fileuploadConfig;
	@Resource
	private FtpImageFileService ftpImageFileService;
	@Resource
	private HwObsService hwObsService;
	@Resource
	private LjdpDfsUtils dfsUtils;
//	@Resource
//	private RedisTemplate<String, Object> redisTemplate;
	
	public LjdpUploadController() {
		// 设置本地服务器存放文件的根路径
		ConfigFile cfg = ConfigFileFactory.getInstance().getAppConfig();
		rootLocation = cfg.getValue("upload_local_path");
		System.out.println("upload_local_path="+rootLocation);
	}
	
	/**
	 * 上传文件，返回文件上传后的信息； 如果文件上传成功，将把结果信息临时存放内存，供后续提交表单请求使用；
	 * 结果信息里的{data}字段记录了内存区中存放的id值
	 * 文件存放路径=${rootLocation}/${busiPath}/${yyyy-MM-dd}/上传的文件
	 * 
	 * @param file
	 * @param busiPath
	 *            存放文件的业务路径
	 * @return
	 */
	@RequestMapping("/memory/upload.act")
	@ResponseBody
	@Security(session=false)
	public Result uploadFile(@RequestParam("file") Part file,
			@RequestParam(value="busiPath",required=false) String busiPath,
			@RequestParam(value="fileBusiType",required=false)String fileBusiType) {
		try {
			String rootPath = rootLocation;
			if(fileuploadConfig != null) {
				if(StringUtils.isNotEmpty(fileuploadConfig.getLocalPath())) {
					rootPath = fileuploadConfig.getLocalPath();
				}
			}
			
			RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
			if(redisTemplate != null) {
				SequenceService cs = ConcurrentSequence.getInstance();
				String fid = cs.getSequence("");
				
				String filename = file.getSubmittedFileName();
				int lastIndex = filename.lastIndexOf(".");
				if(lastIndex <= 0){
					throw new APIException(-10004, "上传文件名称格式有误");
				}
				AttachVO vo = new AttachVO();
				vo.setFid(fid);
				vo.setName(filename);
				vo.setContentType(file.getContentType());
				vo.setContents(ByteUtil.input2byte(file.getInputStream()));
				redisTemplate.boundValueOps(fid).set(vo, 10, java.util.concurrent.TimeUnit.MINUTES);
				Result result = new GeneralResult(true);
				result.setMsg("临时上传成功");
				result.setData(fid);
				return result;
			} else {
				if(busiPath == null) {
					busiPath = "upload";
				}
				System.out.println("rootPath="+rootPath);
				System.out.println("busiPath="+busiPath);
				System.out.println("fileBusiType="+fileBusiType);
				FileUploadTask task = super.saveUploadFile(file, rootPath, busiPath);
				task.setFileBusiType(fileBusiType);
				System.out.println(task.getUploadFileName()+","+task.getUploadContentType()+",save="+task.getSaveFileName());
				BatchResult taskRes = task.getResult();

				if (taskRes.isSuccess()) {
					// 设置附件在内存存放的ID
					SequenceService cs = ConcurrentSequence.getInstance();
					taskRes.setData(cs.getSequence("F"));
					// 在内存临时保存附件信息
					MemoryCache.putData(DictConstants.CACHE_ATTACH, taskRes.getData().toString(), task);
				}
				
				Result result = new GeneralResult(true);
				result.setMsg(taskRes.getMsg());
				result.setData(taskRes.getData());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Result result = new GeneralResult();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			return result;
		}
	}
	
//	@ResponseBody
//	@RequestMapping("/memory/down/{id}")
//	public Result downFile(HttpServletRequest request, HttpServletResponse response
//			,@PathVariable("id")String id) {
//		Result result = new GeneralResult(true);
//		RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
//		if(redisTemplate != null) {
//			AttachVO vo = (AttachVO)redisTemplate.boundValueOps(id).get();
//			if(vo != null) {
//				String ext = FileUtils.getExtName_(vo.getName());
//				String fullFilePath = org.ljdp.util.FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),id+"."+ext);
//				
//				try {
//					FileUtils.writeByteArrayToFile(new File(fullFilePath), vo.getContents());
//					result.setData(fullFilePath);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return result;
//	}
	
	/**
	 * 查看上传的临时文件
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping("/memory/view/{id}")
	public void viewFile(HttpServletRequest request, HttpServletResponse response
			,@PathVariable("id")String id) {
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
		if(redisTemplate != null) {
			AttachVO vo = (AttachVO)redisTemplate.boundValueOps(id).get();
			if(vo != null) {
				response.setContentType(vo.getContentType());
				try {
					response.getOutputStream().write(vo.getContents());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			FileUploadTask task = (FileUploadTask)MemoryCache.getData(
					DictConstants.CACHE_ATTACH, id);
			if(task == null) {
				ExtUtils.writeHtmlFailure("文件不存在", response);
				return;
			}
			InputStream input = null;
			try {
				File file = new File(task.getSaveFileName());
				if(!file.exists()) {
					throw new Exception("文件不存在");
				}
				input = new BufferedInputStream(new FileInputStream(file));
				response.reset();
				response.setContentType(task.getUploadContentType());
				FileUtils.copy(input, response.getOutputStream());
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
	
	/**
	 * 一般给富文本编辑器使用，直接把文件上传到ftp永久保存
	 * @param file
	 * @param busiPath
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ftp/upload.act")
	@ResponseBody
	@Security(session=false)
	public Result uploadFtp(@RequestParam("file") Part file,
			@RequestParam(value="busiPath",required=false) String busiPath,
			@RequestParam(value="bid",required=false) String bid) throws Exception{
		if(StringUtils.isEmpty(busiPath)) {
			busiPath = "rtf";//应该是富文本编辑器使用
		}
		
		BsImageFile f = ftpImageFileService.upload(file, busiPath, bid, 0, SsoContext.getUserId());
		
		ImageResult result = new ImageResult(true);
		result.setFid(f.getId().toString());
		result.setViewUrl(dfsUtils.getViewUrl(f));
		return result;
	}
	
	@RequestMapping("/obs/upload.act")
	@ResponseBody
	@Security(session=true)
	public Result uploadObs(@RequestParam("file") Part file,
			@RequestParam(value="busiPath",required=false) String busiPath,
			@RequestParam(value="bid",required=false) String bid,
			@RequestParam(value="type",required=false) String type) throws Exception{
		if(StringUtils.isEmpty(busiPath)) {
			if(StringUtils.isNotBlank(type)) {
				if(type.equals("vedio")) {
					busiPath = fileuploadConfig.getVedioBucket();
				} else if(type.equals("audio")) {
					busiPath = fileuploadConfig.getAudioBucket();
				}
			}
		}
		BsImageFile imgFile = hwObsService.upload(file, busiPath, bid, null, SsoContext.getUserId());
		
		ImageResult result = new ImageResult(true);
		result.setCode(200);
		result.setFid(imgFile.getId().toString());
		result.setViewUrl(dfsUtils.getViewUrl(imgFile));
		return result;
	}
	
	/**
	 * 导入文件的临时上传
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/obs/upload-import.act")
	@ResponseBody
	@Security(session=true)
	public Result uploadObsImport(@RequestParam("file") Part file) throws Exception{
		String filename = file.getSubmittedFileName();
		int lastIndex = filename.lastIndexOf(".");
		if(lastIndex <= 0){
			throw new APIException(-10004, "上传文件名称格式有误");
		}
		String fileid = hwObsService.uploadOnly(file, "import");
//		AttachVO vo = new AttachVO();
//		vo.setFid(fileid);
//		vo.setName(filename);
//		redisTemplate.boundValueOps(fileid).set(vo, 30, java.util.concurrent.TimeUnit.MINUTES);
		
		Result result = new GeneralResult(true);
		result.setMsg("上传成功");
		result.setData(fileid);
		return result;
	}
	
	@RequestMapping("/obs/down-import.act")
	@ResponseBody
	@Security(session=true)
	public Result downloadObsImport(@RequestParam("fileid")String fileid) throws Exception{
//		AttachVO vo = (AttachVO)redisTemplate.boundValueOps(fileid).get();
//		if(vo == null) {
//			throw new APIException(10001, "文件已过期");
//		}
		
		String filename = hwObsService.downloadByObsKey(fileid, "import");
		Result result = new GeneralResult(true);
		result.setMsg(filename);
//		result.setData(vo.getName());
		return result;
	}
	
	@RequestMapping(value="/obs/delete.act", method=RequestMethod.POST)
	@ResponseBody
	@Security(session=true)
	public Result deleteObsFile(@RequestParam("fid")Long fid) throws Exception{
		hwObsService.removeById(fid);
		Result result = new GeneralResult(true);
		result.setMsg("删除成功");
		return result;
	}
	
	@RequestMapping("/generalId")
	@ResponseBody
	@Security(session=false)
	public ImageResult generalId() {
		ConcurrentSequence cs = ConcurrentSequence.getInstance();
		ImageResult result = new ImageResult(true);
		result.setCode(200);
		result.setFid(cs.getSequence("temp-"));
		return result;
	}
}
