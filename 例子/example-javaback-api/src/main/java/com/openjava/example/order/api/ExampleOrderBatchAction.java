package com.openjava.example.order.api;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.view.spring.AbstractBatchComController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.util.FileUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.example.order.component.ExampleOrderBatchBO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="订单管理-批量操作")
@RestController
@RequestMapping("/example/order/exampleOrder/batch")
public class ExampleOrderBatchAction extends AbstractBatchComController {

	@Resource
	private LjdpFileuploadConfig fileuploadConfig;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private ExampleOrderBatchBO exampleOrderBatchBO;
	
	@ApiOperation(value = "开始导入", nickname="process")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataType = "string", paramType = "post"),
	})
	@RequestMapping(value="/process", method=RequestMethod.POST)
	@Security(session=false)
	public Result doBatchProcess(@RequestParam("fileId")String fileId) {
		//获取文件
		AttachVO vo = (AttachVO)redisTemplate.boundValueOps(fileId).get();
		if(vo == null) {
			Result res = new GeneralResult();
			res.setSuccess(false);
			res.setMsg("文件不存在，可能已超时，请重新上传");
			return res;
		}
		String ext = FileUtils.getExtName_(vo.getName());
		String fullFilePath = FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),fileId+"."+ext);
		redisTemplate.delete(fileId);
		String fileName = vo.getName();
		try {
			FileUtils.writeByteArrayToFile(new File(fullFilePath), vo.getContents());
		} catch (IOException e) {
			e.printStackTrace();
			Result result = new GeneralResult();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			return result;
		}
		//开始处理
//		String fullFilePath = "C:/tempupload/导入测试example-.xlsx";
//		String fileName = "导入测试example-.xlsx";
		String batchType = "订单导入";
		return super.doBatchProcess(exampleOrderBatchBO, fullFilePath,
				fileName, batchType, 0);
	}

	@Override
	protected FileBatchTask getBatchTask(FileBusinessObject bo) {
		FileBatchTask task = super.getBatchTask(bo);
		//第一行是标题，从第二行开始导入，index从0开始
		task.setBeginIndex(1);
		//处理完成后删除原文件
		task.setDeleteAfterProcess(false);
		/*
		 * 一个事务批量提交的数量（当事务回滚，会把当前事务涉及的数据都回滚）
		 *  场景1：主要是把文件数据insert入库，建议设置为100~1000
		 *  场景2：进行业务办理，每行数据都是一次独立的业务办理，建议设置为1
		 *  场景3：整个文件数据是一个单独的业务表单，例如导入一张试卷，建议设置尽量大的值，超出当前预计的表单最大数量，建议>=1000
		 */
		task.setBatchSize(1);
		//如果是excel文件，填写一共有多少个sheet需要处理
		task.setSheetNumber(2);
		return task;
	}
}
