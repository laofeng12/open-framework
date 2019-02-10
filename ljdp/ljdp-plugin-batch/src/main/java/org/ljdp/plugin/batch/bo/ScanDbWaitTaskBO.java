package org.ljdp.plugin.batch.bo;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.session.Request;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.query.RO;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.model.TaskInfoVO;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 后台定时启动BO，扫描待处理的任务。
 * @author hzy
 *
 */
public class ScanDbWaitTaskBO implements BusinessObject {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private int scanCount = 0;
	
	private String scanState;
	private String ipLimit;

	public synchronized Object doBusiness(Object... arg0) throws Exception {
		Request request = null;
		if(arg0 != null) {
			if(arg0.length >= 1) {
				request = (Request)arg0[0];
			}
		}
		if(request != null) {
			scanState = request.getParameter("scan.state");
			ipLimit = request.getParameter("scan.iplimit");
		}
		if(StringUtils.isEmpty(scanState)) {
			scanState = "ALL";
		}
		if(StringUtils.isEmpty(ipLimit)) {
			ipLimit = "true";
		}
		if(scanCount <= 0) {
			scanCount++;
		}
		log.info("开始扫描后台任务：scan.state="+scanState+",scan.iplimit="+ipLimit);
		CommonService<BtFileImportTask> server = ServiceFactory.buildCommon(BtFileImportTask.class);
		if(scanState.equalsIgnoreCase("all")) {
			//先查找处理中状态的，有可能宕机后未处理完
			scanDBTask(server, BatchFileDic.PROCESSING, Boolean.valueOf(ipLimit));
		}
		//查找等待处理中的任务
		scanDBTask(server, BatchFileDic.WAIT, Boolean.valueOf(ipLimit));
		return null;
	}

	private synchronized void scanDBTask(CommonService<BtFileImportTask> server, String state, boolean ipLimit) throws Exception {
		String hostAddress = InetAddress.getLocalHost().getHostAddress();
		DBQueryParam param = new DBQueryParam();
		param.addQueryCondition("procState", RO.EQ, state);
		if(ipLimit) {
			param.addQueryCondition("serverIP", RO.EQ, hostAddress);
		}
		param.addQueryCondition("procWay", RO.EQ, BatchFileDic.PW_FB_B);
		param.addSort("createDate", "asc");
		Collection<BtFileImportTask> processList = server.doDatas(param);
		Iterator<BtFileImportTask> processIt = processList.iterator();
		while (processIt.hasNext()) {
			BtFileImportTask task = processIt.next();
			if(!TaskPoolManager.getBsPool().containsTask(task.getTaskId())) {
				//把任务添加到后台线程池
				try {
					addTaskToThreadPool(task);
					log.info("添加后台任务："+task.getTaskName()+", "+task.getTaskId());
				} catch (Exception e) {
					log.error("添加后台任务失败："+task.getTaskName()+", "+task.getTaskId(), e);
				}
			}
		}
	}
	
	private synchronized void addTaskToThreadPool(BtFileImportTask db) throws Exception{
		TaskInfoVO task = new TaskInfoVO(db.getTaskId(), db.getTaskName());
		task.setBsBusinessObject(db.getBsBo());
		task.setOperatorId(db.getOperatorId());
		task.setOperatorAccount(db.getOperator());
		task.setOperatorName(db.getOperatorName());
		task.setOperType(db.getOperType());
		task.setType(db.getTaskType());
		if(db.getTotalRec() != null) {
			task.setTotalNum(db.getTotalRec());
		} else {
			task.setTotalNum(0);
		}
		if(db.getSuccRec() != null) {
			task.setSuccessNum(db.getSuccRec());
		} else {
			task.setSuccessNum(0);
		}
		if(db.getFailRec() != null) {
			task.setFailNum(db.getFailRec());
		} else {
			task.setFailNum(0);
		}
		TaskPoolManager.addBsBatchTask(task);
	}

	public String getScanState() {
		return scanState;
	}

	public void setScanState(String scanState) {
		this.scanState = scanState;
	}

	public String getIpLimit() {
		return ipLimit;
	}

	public void setIpLimit(String ipLimit) {
		this.ipLimit = ipLimit;
	}

}
