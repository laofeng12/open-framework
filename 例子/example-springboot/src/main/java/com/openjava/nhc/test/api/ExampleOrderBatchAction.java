package com.openjava.nhc.test.api;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Part;

import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.pool.MemoryTaskPool;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.plugin.batch.view.spring.AbstractBatchComController;
import org.ljdp.secure.annotation.Security;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.util.FileUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.openjava.nhc.test.component.ExampleOrderBatchBO;
import com.openjava.nhc.test.service.ExampleOrderService;

/**
 * 导入api接口
 * @author 自由
 *
 */
@Api(tags="批量导入")
@RestController
@RequestMapping("/nhc/test/exampleOrder/batch")
public class ExampleOrderBatchAction extends AbstractBatchComController {
	@Resource
	private LjdpFileuploadConfig fileuploadConfig;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private ExampleOrderBatchBO exampleOrderBatchBO;
	@Resource
	private ExampleOrderService exampleOrderService;
	
	//方案一：分2步上传文件
	@ApiOperation(value = "开始导入", nickname="process")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataType = "string", paramType = "post"),
	})
	@RequestMapping(value="/process")
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
		//也可以自己创建业务对象传入参数（因为用spring注入默认是单例，如果传入参数不能用单例，防止并发任务参数被覆盖）
//		ExampleOrderBatchBO exampleOrderBatchBO2 = new ExampleOrderBatchBO();
//		exampleOrderBatchBO2.set
		
		//开始处理
//		String fullFilePath = "C:/tempupload/导入测试example-.xlsx";
//		String fileName = "导入测试example-.xlsx";
		String batchType = "订单管理导入";
		return super.doBatchProcess(exampleOrderBatchBO, fullFilePath,
				fileName, batchType, 0);
	}
	
	//方案二：直接上传文件
//	@ApiOperation(value = "开始导入", nickname="process")
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "file1", value = "文件", required = true, dataType = "file", paramType = "post"),
//	})
//	@RequestMapping(value="/process2", method=RequestMethod.POST)
//	@Security(session=false)
//	public Result doBatchProcess2(@RequestParam("file1") Part file1) throws Exception{
//		//1、先保存到服务器
//        String fullFilePath1 = FileUtils.joinDirectory(fileuploadConfig.getLocalPath(),file1.getSubmittedFileName());
//        file1.write(fullFilePath1);
//        
//		String batchType = "订单管理导入";
//		return super.doBatchProcess(exampleOrderBatchBO, fullFilePath1,
//				file1.getSubmittedFileName(), batchType, 0);
//	}
	

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
		task.setSheetNumber(1);
		return task;
	}
	
	@RequestMapping(value="/task/{taskId}", method= RequestMethod.GET)
    @Security(session=true)
    public BaseBatchTask getTask(@PathVariable("taskId")String taskId) {
		BaseBatchTask task = TaskPoolManager.getFgPool().getTaskByID(taskId);
		if(task == null) {
			//任务已经结束放到临时内存
//			task = MemoryTaskPool.getTask(taskId);
			//分布式环境下，任务不在当前节点，去redis获取
			task = (BaseBatchTask)redisTemplate.opsForValue().get(taskId);
		}
		if(task == null) {
			//任务已经结束长时间了，不在内存里，从数据库读取
		}
		//获取当前任务进度%
		task.getPercent();
		//已处理记录数量
		task.getCurrentRecord();
		//成功数量
		task.getOk();
		//失败数量
		task.getFail();
		//总数量
		task.getTotalRecords();
		//是否已开始
		task.isStarted();
		//是否处理中
		task.isRunning();
		//文件是否已处理完成
		task.isCompleted();
		//任务是否已结束
		task.isFinished();
		return task;
	}
}
