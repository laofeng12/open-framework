package org.ljdp.plugin.batch.view;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.json.JSONTools;
import org.ljdp.common.timer.ScheduleManager;
import org.ljdp.component.session.Request;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.core.db.DataPackage;
import org.ljdp.plugin.batch.model.Batch;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.model.TaskInfoVO;
import org.ljdp.plugin.batch.pool.MemoryTaskPool;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.ui.extjs.ExtUtils;

public  class BaseBatchAction extends FileUploadAction {
	private static final long serialVersionUID = -9223216667148415779L;
	
	// 批量处理时的参数
	private String batchID;// 标识此次批量处理的唯一ID
	private String batchName = "BATCH";// 批量处任务的名称


	private String show;// 页面显示模式
	protected int batchOperation; // 开始，暂停，停止等操作码

	public BaseBatchAction() {
		super();
	}
	
	public BaseBatchAction(String batchName) {
		super();
		this.batchName = batchName;
	}

	public BaseBatchAction(String batchName, String uploadName) {
		super(uploadName);
		this.batchName = batchName;
	}

	protected String getBatchTaskName(String id) {
		return batchName + "_" + id;
	}

	//获取任务状态，用于进度条更新
	public void batchState() {
		BaseBatchTask bean = TaskPoolManager.getFgPool().getTaskByID(getBatchID());
		if(bean == null) {
			bean = TaskPoolManager.getBsPool().getTaskByID(getBatchID());
		}
		if (bean != null) {
			if (batchOperation == Batch.OPER_STOP) {
				bean.stop();
			}
//			if (bean.isFinished()) {
//			}
		} else {
			//查找本地缓存
			bean = MemoryTaskPool.getTask(getBatchID());
			
			if(bean == null || !bean.getWay().equals(BatchFileDic.PW_F)) {
				//查找远程后台是否存在任务
				String beanJson = getRemoteBsBatchState();
				if(StringUtils.isNotEmpty(beanJson)) {
					//远程存在
					MemoryTaskPool.removeTask(getBatchID());
					getResponse().setContentType("text/json;charset=utf-8");
					try {
						getResponse().getWriter().write(beanJson);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		JSONTools.writePage(bean, getResponse());
	}

	protected String getRemoteBsBatchState() {
		ConfigFile corecfg = Env.current().getConfigFile();
		String remoteStUrl = corecfg.getValue("batch.bs.state.url");
		if(StringUtils.isNotBlank(remoteStUrl)) {
			//从远程后台获取任务进度
			remoteStUrl += "?batchID="+getBatchID()+"&batchOperation="+batchOperation;
			LjdpHttpClient httpClient = new LjdpHttpClient();
			try {
				HttpResponse response = HttpClientUtils.httpGetURL(httpClient.getHttpClient(), remoteStUrl);
				String beanJson = HttpClientUtils.getContentString(response.getEntity(), "utf-8");
				if(beanJson.equals("null")) {
					return null;
				}
				return beanJson;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取后台任务的状态
	 */
	public void bsBatchState() {
		BaseBatchTask bean = TaskPoolManager.getBsPool().getTaskByID(getBatchID());
		if (bean != null) {
			if (batchOperation == Batch.OPER_STOP) {
				bean.stop();
			}
		} else {
			bean = MemoryTaskPool.getTask(getBatchID());
		}
		JSONTools.writePage(bean, getResponse());
	}
	
	//获取所有可见的正在处理的任务列表
	public void batchList() {
		DataPackage<TaskInfoVO> dp = new DataPackage<TaskInfoVO>();
		String userId = null;
		if( !isAdmin() ) {
			userId = getUser().getId();
		}
		ArrayList<TaskInfoVO> list = TaskPoolManager.getFgPool().queryByUser(userId);
		list.addAll(TaskPoolManager.getBsPool().queryByUser(userId));
		list.addAll(MemoryTaskPool.queryByUser(userId));
		dp.setTotalCount(list.size());
		dp.setDatas(list);
		ExtUtils.writeJSONGrid(dp, getResponse());
	}
	
	/**
	 * 通知后台任务管理器扫描
	 */
	public void notifyBsTaskScan() {
		//5秒后启动后台任务扫描器
		Request request = new Request(getBatchID());
		request.addParameter("scan.state", BatchFileDic.WAIT);
		request.addParameter("scan.iplimit", "false");
		boolean flag = ScheduleManager.schedule("BATCH-FILEIMPORT-BS", 5, request);
		if(flag) {
			ExtUtils.writeSuccess(getResponse());
		} else {
			ExtUtils.writeFailure("task not exists", getResponse());
		}
	}

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		setUploadID(batchID);
		this.batchID = batchID;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public int getBatchOperation() {
		return batchOperation;
	}

	public void setBatchOperation(int batchOperation) {
		this.batchOperation = batchOperation;
	}

}
