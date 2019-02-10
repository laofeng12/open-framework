package org.ljdp.plugin.batch.view;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.json.JSONTools;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.core.db.DataPackage;
import org.ljdp.module.filetask.FileBatchTask;
import org.ljdp.plugin.batch.model.Batch;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.plugin.batch.task.LJDPFileBatchTask;
import org.ljdp.ui.extjs.ExtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBatchFileAction extends BaseBatchAction {
	private static final long serialVersionUID = -3685782141128949569L;
	private Logger log = LoggerFactory.getLogger(AbstractBatchFileAction.class);
	private String batchFileName;
	private Integer totalCount;
	private FileBatchTask batchTask;
	private boolean autoBatchSize = true;
	private String submitParams;//页面提交的自定义参数
	
	//true是任务利用HttpSession保存和管理，false时使用BatchTaskManager管理
	private boolean sessionScope = false;

	public AbstractBatchFileAction() {
		super();
	}
	
	public AbstractBatchFileAction(String batchName) {
		super(batchName);
	}

	public AbstractBatchFileAction(String batchName, String uploadName) {
		super(batchName, uploadName);
	}

	/**
	 * 取当前任务的处理程序，有子类实现
	 */
	protected FileBatchTask getBatchTask() {
		if(batchTask == null) {
			FileBusinessObject bo = getBusinessObject();
			if(bo != null) {
				batchTask = new LJDPFileBatchTask(bo);
				try {
					BeanUtils.setProperty(bo, "cursor", batchTask.getCursor());
					BeanUtils.setProperty(bo, "user", getUser());
					BeanUtils.setProperty(bo, "fileBatchTask", batchTask);
					if(submitParams != null) {
						BeanUtils.setProperty(bo, "submitParams", submitParams);
					}
				} catch (Exception e) {
					log.error("设置业务对象参数失败",e);
				}
				if(StringUtils.isNotBlank(submitParams)) {
					String[] items = submitParams.split("&");
					for (String item : items) {
						String[] pt = item.split("=");
						try {
							BeanUtils.setProperty(bo, pt[0], pt[1]);
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return batchTask;
	}
	
	abstract protected FileBusinessObject getBusinessObject();

	/**
	 * 开始一个处理文件的任务
	 */
	public void batchProcess() {
		if(sessionScope) {
			getSession().removeAttribute(getBatchTaskName(getBatchID()));
		}
		try {
			//由子类返回实际任务处理类
			FileBatchTask batch = getBatchTask();
			if(batch != null) {
				beginBatchTask(batch);
			} else {
				ExtUtils.writeFailure("初始化任务失败", getResponse());
			}
		} catch (Throwable e) {
			log.error("开始任务时发生异常", e);
			ExtUtils.writeFailure(e.getMessage(), getResponse());
		}
	}

	protected void beginBatchTask(FileBatchTask batch) throws Exception {
		initializeBatchTask(batch);
//		System.out.println("begin----"+getBatchTaskName(getBatchID()));
		//开始前的检查和初始化等操作
		if(onBeforeProcess()) {
			beginProcess(batch);
			ExtUtils.writeSuccess(batch.getId(), getResponse());
		} else {
			ExtUtils.writeFailure("任务开始失败，前置条件检查不通过", getResponse());
		}
	}

	protected void initializeBatchTask(FileBatchTask batch) {
		//设置任务主键ID
		if(StringUtils.isBlank(batch.getId())) {
			if(StringUtils.isEmpty(getBatchID())) {
				setBatchID(ConcurrentSequence.getInstance().getSequence(""));
			}
			batch.setId(getBatchID());
		}
		//设置任务的操作者
		if(batch.getUser() == null) {
			batch.setUser(getUser());
		}
		//设置任务类型
		if(StringUtils.isEmpty(batch.getType())) {
			batch.setType(getBatchName());
		}
		//前台处理
		batch.setWay(BatchFileDic.PW_F);
		//设置处理的文件全路径
		batch.setFilename(batchFileName);
		batch.setUploadFileName(getUploadFileFileName());
		batch.setName(batch.getUploadFileName());
		batch.setAutoBatchSize(isAutoBatchSize());
		//获取文件总需要处理的行数
		if (totalCount != null && totalCount.intValue() > 0) {
			batch.setTotalRecords(totalCount);
		} else {
			batch.updateTotalRecords();
		}
		try {
			BusinessObject bo = (BusinessObject)PropertyUtils.getProperty(batch, "bo");
			if(BeanUtils.getProperty(bo, "operType") == null) {
				BeanUtils.setProperty(bo, "operType", batch.getOperType());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void beginProcess(BaseBatchTask batch) throws Exception {
		//开始任务
		if(sessionScope) {
			getSession().setAttribute(getBatchTaskName(getBatchID()), batch);
			Thread batchThread = new Thread(batch);
			batchThread.start();
		} else {
			if(TaskPoolManager.getFgPool().containsTask(batch.getId())) {
				throw new Exception("任务管理中已经存在和此任务相同ID的任务处理中");
			}
			TaskPoolManager.getFgPool().addTask(batch);
		}
	}
	
	//获取任务状态，用于进度条更新
	@Override
	public void batchState() {
		if(sessionScope) {
			BaseBatchTask bean = (BaseBatchTask) getSession().getAttribute(
					getBatchTaskName(getBatchID()));
			if (bean != null) {
				if (batchOperation == Batch.OPER_STOP) {
					bean.stop();
				}
				if (bean.isFinished()) {
					getSession().removeAttribute(getBatchTaskName(getBatchID()));
				}
			}
			JSONTools.writePage(bean, getResponse());
		} else {
			super.batchState();
		}
	}
	
	//获取所有可见的正在处理的任务列表
	@Override
	public void batchList() {
		if(sessionScope) {
			DataPackage<BaseBatchTask> dp = new DataPackage<BaseBatchTask>();
			BaseBatchTask bean = (BaseBatchTask) getSession().getAttribute(
					getBatchTaskName(getBatchID()));
			dp.setTotalCount(1);
			ArrayList<BaseBatchTask> list = new ArrayList<BaseBatchTask>();
			list.add(bean);
			dp.setDatas(list);
			ExtUtils.writeJSONGrid(dp, getResponse());
		} else {
			super.batchList();
		}
	}

	@Override
	public String uploadFile() {
		if(sessionScope) {
			getSession().removeAttribute(getBatchTaskName(getBatchID()));
		}
		return super.uploadFile();
	}

	@Override
	public void uploadFileNow() {
		if(sessionScope) {
			getSession().removeAttribute(getBatchTaskName(getBatchID()));
		}
		super.uploadFileNow();
	}

	/**
	 * 事件：开始任务前运行，返回false则不开始任务
	 */
	protected boolean onBeforeProcess() {
		return true;
	}

	public String getBatchFileName() {
		return batchFileName;
	}

	public void setBatchFileName(String batchFileName) {
		this.batchFileName = batchFileName;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isSessionScope() {
		return sessionScope;
	}

	public void setSessionScope(boolean sessionScope) {
		this.sessionScope = sessionScope;
	}

	public void setBatchTask(FileBatchTask batchTask) {
		this.batchTask = batchTask;
	}

	public boolean isAutoBatchSize() {
		return autoBatchSize;
	}

	public void setAutoBatchSize(boolean autoBatchSize) {
		this.autoBatchSize = autoBatchSize;
	}

	public String getSubmitParams() {
		return submitParams;
	}

	public void setSubmitParams(String submitParams) {
		this.submitParams = submitParams;
	}

}
