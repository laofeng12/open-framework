package org.ljdp.plugin.batch.bo;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.ljdp.component.result.DataBaseResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.task.DbBatchTask;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.query.RO;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.module.batch.TransactionDbBatchBO;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.persistent.BtFileData;
import org.ljdp.plugin.batch.persistent.BtFileDataFail;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 后台导入方式入库成功后开始处理的BO。
 * 此BO仅提供更新任务状态表等通用操作，实际处理的BO需要继承此BO。
 * @author hzy
 *
 */
public abstract class BaseDBTaskBO extends TransactionDbBatchBO {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private CommonService<BtFileData> fileDataServer;

	@Override
	public Boolean initialization() {
		//更新导入日志表状态为处理中
		try {
			if(super.initialization()) {
				fileDataServer = ServiceFactory.buildCommon(BtFileData.class);
				
				CommonService<BtFileImportTask> server = ServiceFactory.buildCommon(BtFileImportTask.class);
				BtFileImportTask dbLog = server.doFindByPK(getBatchID());
				if(!dbLog.getProcState().equals(BatchFileDic.PROCESSING)) {
					dbLog.setProcState(BatchFileDic.PROCESSING);
					dbLog.setBeginTime(new Date());
					server.doUpdate(dbLog);
				}
				return true;
			}
		} catch (Exception e) {
			log.error("初始化任务失败", e);
		}
		return false;
	}

	@Override
	public Boolean finalWork(DbBatchTask task) {
		//更新导入日志表状态为完成
		try {
			CommonService<BtFileImportTask> taskServer = ServiceFactory.buildCommon(BtFileImportTask.class);
			BtFileImportTask dbLog = taskServer.doFindByPK(getBatchID());
			if(dbLog.getProcState().equals(BatchFileDic.SUSPEND)) {
				return true;
			}
			int processNum = dbLog.getSuccRec() + dbLog.getFailRec();
			if(processNum < dbLog.getTotalRec()) {
				dbLog.setProcState(BatchFileDic.FAILEND);
				dbLog.setFailLog("部分数据已丢失");
			} else {
				dbLog.setProcState(BatchFileDic.FINISH);
			}
			dbLog.setCostTime(task.getUseTime());
			dbLog.setProcWay(BatchFileDic.PW_FB);
			dbLog.setFinishTime(new Date());
			taskServer.doUpdate(dbLog);
			return true;
		} catch (Exception e) {
			log.error("结束任务时更新日志失败", e);
		}
		return false;
	}

	@Override
	public int countWaitDatas() throws Exception {
		DBQueryParam waitDataParam = new DBQueryParam();
		waitDataParam.addQueryCondition("taskId", RO.EQ, getBatchID());
		return fileDataServer.doCount(waitDataParam);
	}

	@Override
	public Collection<BtFileData> queryWaitDatas(int size) throws Exception{
		DBQueryParam waitDataParam = new DBQueryParam();
		waitDataParam.setPageno(1);
		waitDataParam.setPagesize(size);
		waitDataParam.addQueryCondition("taskId", RO.EQ, getBatchID());
		waitDataParam.addSort("dataId", "asc");
		return fileDataServer.doDatas(waitDataParam);
	}

	@Override
	protected void onProcessComplete(Object record, Result result) throws Exception {
		BtFileData data = (BtFileData)record;
		String sql = "delete BtFileData where dataId=:DATAID";
		Map<String, Object> dataParam = new HashMap<String, Object>();
		dataParam.put("DATAID", data.getDataId());
		fileDataServer.doUpdate(sql, dataParam);
		
		Map<String, Object> taskParam = new HashMap<String, Object>();
		taskParam.put("TASKID", data.getTaskId());
		if(result.isSuccess()) {
			String succSQL = "update BtFileImportTask set succRec=succRec+1 where taskId=:TASKID";
			fileDataServer.doUpdate(succSQL, taskParam);
		} else {
			String failSQL = "update BtFileImportTask set failRec=failRec+1 where taskId=:TASKID";
			fileDataServer.doUpdate(failSQL, taskParam);
		}
	}

	@Override
	protected void onProcessFail(Object record, Result result) throws Exception {
		BtFileData data = (BtFileData)record;
		BtFileDataFail failRec = new BtFileDataFail();
		failRec.setDataId(data.getDataId());
		failRec.setDataItem(data.getDataItem());
		failRec.setTaskId(data.getTaskId());
		failRec.setFailReason(result.getMsg());
		fileDataServer.doCreate(failRec);
	}

	@Override
	protected DataBaseResult doProcessRecord(Object record) {
		BtFileData data = (BtFileData)record;
		return doProcessLine(data.getDataItem());
	}
	
	protected abstract DataBaseResult doProcessLine(String line);

	@Override
	public void destory() {
		fileDataServer.flush();
		fileDataServer.clear();
	}

}
