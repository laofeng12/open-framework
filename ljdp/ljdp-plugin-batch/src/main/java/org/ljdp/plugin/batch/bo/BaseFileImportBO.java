package org.ljdp.plugin.batch.bo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.timer.ScheduleManager;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.session.Request;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.model.TaskInfoVO;
import org.ljdp.plugin.batch.persistent.BtFileData;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.task.LJDPFileBatchBO;
import org.ljdp.plugin.batch.task.LJDPFileBatchTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 后台导入方式前台入库BO
 * @author hzy
 *
 */
public class BaseFileImportBO extends LJDPFileBatchBO {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected BatchResult doProcessRecord(String record) {
		LJDPFileBatchTask task = (LJDPFileBatchTask)getFileBatchTask();
		
		SequenceService ss = ConcurrentSequence.getCentumInstance();
		BtFileData data = new BtFileData();
		data.setDataId(ss.getSequence());
		data.setDataItem(record);
		data.setTaskId(task.getId());
		
		BatchResult result = new GeneralBatchResult();
		try {
			gs.doCreate(data);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
			log.error("doProcessRecord-", e);
		}
		return result;
	}
	
	@Override
	protected BatchResult doProcessRecord(int sheetLocation, String record) {
		if(sheetLocation == 0) {
			return doProcessRecord(record);
		} else {
			BatchResult result = new GeneralBatchResult();
			result.setSuccess(false);
			result.setMsg("不支持第"+sheetLocation+"个sheet");
			return result;
		}
	}

	@Override
	public String getTitle() {
		
		return "标题";
	}

	/**
	 * 登记导入日志表
	 */
	@Override
	public Boolean initialization() {
		if(!super.initialization()) {
			return false;
		}
		TaskInfoVO task = getBatchTaskInfo();
		BtFileImportTask dbLog = new BtFileImportTask();
		dbLog.setTaskId(task.getId());
		dbLog.setTaskName(task.getName());
		dbLog.setTaskType(task.getType());
		dbLog.setOperType(task.getOperType());
		dbLog.setOperatorId(task.getOperatorId());
		dbLog.setOperator(task.getOperatorAccount());
		dbLog.setOperatorName(task.getOperatorName());
		dbLog.setBsBo(task.getBsBusinessObject());
		dbLog.setCreateDate(new Date());
		dbLog.setTotalRec(getCursor().getTotalRecords());
		dbLog.setProceFileName(task.getProcessFileName());
		dbLog.setBeginTime(new Date());
		try {
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			dbLog.setServerIP(hostAddress);
		} catch (UnknownHostException e1) {
			log.info("获取服务器IP失败", e1);
			return false;
		}
		
		if(StringUtils.isNotBlank(dbLog.getBsBo())) {
			dbLog.setProcWay(BatchFileDic.PW_FB_F);
			dbLog.setProcState(BatchFileDic.IMPORTING);
		} else {
			dbLog.setProcWay(BatchFileDic.PW_F);
			dbLog.setProcState(BatchFileDic.PROCESSING);
		}
		try {
			gs.doCreate(dbLog);
			return true;
		} catch (Exception e) {
			log.error("文件导入初始化失败", e);
		}
		return false;
	}

	/**
	 * 更新导入日志表状态
	 */
	@Override
	public Boolean finalWork() {
		TaskInfoVO task = getBatchTaskInfo();
		try {
			CommonService<BtFileImportTask> server = ServiceFactory.buildCommon(BtFileImportTask.class);
			BtFileImportTask dbLog = server.doFindByPK(task.getId());
			if(dbLog == null) {
				return false;
			}
			dbLog.setTotalRec(task.getTotalNum());
			dbLog.setFailRec(task.getFailNum());
			dbLog.setCostTime(task.getUserTime());
			dbLog.setSuccRec(0);
			if(task.getFailNum() > 0) {
				dbLog.setSuccRec(task.getSuccessNum());
				if(dbLog.getProcWay().equals(BatchFileDic.PW_F)) {
					dbLog.setProcState(BatchFileDic.FINISH);
				} else {
					dbLog.setProcState(BatchFileDic.IMPORTERROR);
				}
				dbLog.setFinishTime(new Date());
				dbLog.setFailLog(task.getFailLog());
				server.doUpdate("delete BtFileData where taskId='"+task.getId()+"'");
			} else {
				if(dbLog.getProcWay().equals(BatchFileDic.PW_F)) {
					dbLog.setSuccRec(task.getSuccessNum());
					dbLog.setFinishTime(new Date());
					if(task.getTotalNum() != (task.getSuccessNum() + task.getFailNum() ) ) {
						//可能初始化步骤失败，或数据丢失
						dbLog.setProcState(BatchFileDic.FAILEND);
					} else {
						dbLog.setProcState(BatchFileDic.FINISH);
					}
				} else {
					if(task.getTotalNum() != task.getSuccessNum()) {
						dbLog.setSuccRec(task.getSuccessNum());
						dbLog.setFinishTime(new Date());
						dbLog.setProcState(BatchFileDic.IMPORTERROR);
					} else {
						dbLog.setProcState(BatchFileDic.WAIT);
						dbLog.setProcWay(BatchFileDic.PW_FB_B);
					}
				}
			}
			server.doUpdate(dbLog);
			
			if(dbLog.getProcState().equals(BatchFileDic.WAIT)) {
				try {
					//5秒后启动后台任务扫描器
					Request request = new Request(dbLog.getTaskId());
					request.addParameter("scan.state", BatchFileDic.WAIT);
					boolean f = ScheduleManager.schedule("BATCH-FILEIMPORT-BS", 5, request);
					if(!f) {
						ConfigFile corecfg = Env.current().getConfigFile();
						String remoteUrl = corecfg.getValue("batch.bs.notifyscan.url");
						if(StringUtils.isNotBlank(remoteUrl)) {
							remoteUrl += "?batchID="+dbLog.getTaskId();
							//后台不在当前应用中，使用远程调用
							LjdpHttpClient httpClient = new LjdpHttpClient();
							HttpResponse response = HttpClientUtils.httpGetURL(httpClient.getHttpClient(), remoteUrl);
							log.info("batch.bs.notify.result:"+HttpClientUtils.getContentString(response.getEntity(),"gbk"));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("触发后台任务扫描失败", e);
				}
			}
			
			return true;
		} catch (Throwable e) {
			log.error("文件导入设置完成状态时失败", e);
			return false;
		}
	}

	@Override
	public void destory() {
		gs.flush();
		gs.clear();
	}

}
