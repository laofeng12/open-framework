package org.ljdp.support.batch.controller;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.plugin.batch.view.spring.LjdpBatchController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.util.FileUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ljdp/filebatch")
public class FileBatchProceAction extends LjdpBatchController {

	@Resource
	private LjdpFileuploadConfig fileuploadConfig;
	
	@RequestMapping("/process.act")
	@ResponseBody
	@Security(session=true)
	public Result doBatchProcess(@RequestParam("fileId")String fileId,
			@RequestParam("component")String component,
			@RequestParam("batchType")String batchType,
			@RequestParam(value="submitParams",required=false)String submitParams) {
		RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
		if(redisTemplate != null) {
			AttachVO vo = (AttachVO)redisTemplate.boundValueOps(fileId).get();
			if(vo != null) {
				String ext = FileUtils.getExtName_(vo.getName());
				String fullFilePath = org.ljdp.util.FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),fileId+"."+ext);
				
				try {
					FileUtils.writeByteArrayToFile(new File(fullFilePath), vo.getContents());
					redisTemplate.delete(fileId);
					return super.doBatchProcess(component, submitParams, fullFilePath,
							vo.getName(), batchType, 0);
				} catch (IOException e) {
					e.printStackTrace();
					Result result = new GeneralResult();
					result.setSuccess(false);
					result.setMsg(e.getMessage());
					return result;
				}
			} else {
				Result res = new GeneralResult();
				res.setSuccess(false);
				res.setMsg("文件不存在，可能已超时，请重新上传");
				return res;
			}
		} else {
			FileUploadTask task = (FileUploadTask) MemoryCache.getData(
					DictConstants.CACHE_ATTACH, fileId);
			if (task == null) {
				Result res = new GeneralResult();
				res.setSuccess(false);
				res.setMsg("文件不存在，可能已超时，请重新上传");
				return res;
			}
			MemoryCache.removeData(DictConstants.CACHE_ATTACH, fileId);
			return super.doBatchProcess(component, submitParams, task.getSaveFileName(),
					task.getUploadFileName(), batchType, 0);
		}
	}
}
