package org.ljdp.plugin.batch.view.spring;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.plugin.batch.task.LJDPFileBatchTask;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.spring.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件组件处理器基础层
 * @author hzy0769
 *
 */
public abstract class AbstractBatchComController extends BaseController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected FileBatchTask getBatchTask(FileBusinessObject bo) {
		FileBatchTask batchTask = new LJDPFileBatchTask(bo);
		try {
			BeanUtils.setProperty(bo, "cursor", batchTask.getCursor());
			BeanUtils.setProperty(bo, "user", getUser());
			BeanUtils.setProperty(bo, "userInfo", SsoContext.getUser());
			BeanUtils.setProperty(bo, "fileBatchTask", batchTask);
		} catch (Exception e) {
			log.error("设置业务对象参数失败",e);
		}
		batchTask.setShowRate(true);//默认后台打印进度
		return batchTask;
	}
	
	protected void initializeBatchTask(FileBatchTask batch, 
			String fileFullName, String fileName, String batchType, int totalCount) {
		//设置任务主键ID
		batch.setId(ConcurrentSequence.getInstance().getSequence(""));
		//设置任务的操作者
		if(batch.getUser() == null) {
			batch.setUser(SsoContext.getUser());
		}
		if(batch.getUser() == null) {
			batch.setUser(getUser());
		}
		//设置任务类型
		batch.setType(batchType);
		//前台处理
		batch.setWay(BatchFileDic.PW_F);
		//设置处理的文件全路径
		batch.setFilename(fileFullName);
		batch.setUploadFileName(fileName);
		batch.setName(fileName);
		batch.setAutoBatchSize(false);
		//获取文件总需要处理的行数
		if (totalCount > 0) {
			batch.setTotalRecords(totalCount);
		} else {
			batch.updateTotalRecords();
			log.info(fileFullName+",beginIndex="+batch.getBeginIndex()+",totalrecords="+batch.getTotalRecords());
		}
		try {
			Object bo = PropertyUtils.getProperty(batch, "bo");
			if(BeanUtils.getProperty(bo, "operType") == null) {
				BeanUtils.setProperty(bo, "operType", batch.getOperType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void beginProcess(BaseBatchTask batch) throws Exception {
		if(TaskPoolManager.getFgPool().containsTask(batch.getId())) {
			throw new Exception("任务管理中已经存在和此任务相同ID的任务处理中");
		}
		TaskPoolManager.getFgPool().addTask(batch);
	}
	
	protected void beginBatchTask(FileBatchTask batch,
			String fileFullName, String fileName,String batchType, int totalCount) throws Exception {
		initializeBatchTask(batch, fileFullName, fileName,batchType, totalCount);
		
		beginProcess(batch);
	}
	
	/**
	 * 接收文件请求，提交文件至批处理任务
	 * @param fsBusiCompent 前台业务对象（负责把文件数据存至数据库）
	 * @param bsBusiCompent 后台业务对象（负责读数据库，执行实际业务）
	 * @param submitParams  提交参数
	 * @param fileFullName  完整文件路径
	 * @param fileName		上传的文件名
	 * @param totalCount    文件行数
	 * @return
	 */
	public Result doBatchProcess(FileBusinessObject bo,  
			String fileFullName, String fileName,String batchType, int totalCount) {
		Result res = new GeneralResult();
		try {
			FileBatchTask batch = getBatchTask(bo);
			beginBatchTask(batch, fileFullName, fileName, batchType, totalCount);
			res.setSuccess(true);
		} catch (Exception e) {
			log.error("doBatchProcess", e);
			res.setSuccess(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}
}
