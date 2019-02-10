package org.ljdp.component.task;

import java.util.Collection;
import java.util.Iterator;

import org.ljdp.component.result.DataBaseResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DbBatchTask extends BaseBatchTask {
	private static final long serialVersionUID = 462915036955630101L;
	private Logger log = LoggerFactory.getLogger(DbBatchTask.class);
	
	@Override
	public void run() {
		try {
			start();
			if(!initialization()) {
				result.setSuccess(false);
				result.setMsg("初始化失败");
				return;
			}
			result = new GeneralBatchResult();
			try {
				if(getTotalRecords() == 0) {
					setTotalRecords(countWaitDatas());
				}
				work();
				Boolean finalSucc = false;
				if(isRunning()) {
					finalSucc = finalWork(this);
					addCurrentProcedure(1);
				}
				result.setSuccess(finalSucc);
				if(!finalSucc) {
					result.setMsg("文件处理成功，结束步骤时出错，可能更新日志失败");
				}
				result.setTotalCount(getTotalRecords());
				result.setCurrent(getCurrentRecord());
			} catch (Throwable ex) {
				while (ex.getCause() != null) {
					ex = ex.getCause();
				}
				log.error(ex.getMessage(), ex);
				result.setSuccess(false);
				result.setMsg(ex.toString());
			}
		} finally {
			destory();
			finish();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void work() throws Exception {
		int totalSize = countWaitDatas();
		super.setTotalRecords(totalSize);
		while(totalSize > 0 && isRunning()) {
			Collection collections = queryWaitDatas(getBatchSize());
			if(collections == null) {
				setRunning(false);
				break;
			}
			Iterator it = collections.iterator();
			while(it.hasNext()) {
				Object record = it.next();
				DataBaseResult result = doProcess(record);
				if(!result.isRollback()) {
					if(result.isSuccess()) {
						setOk(getOk()+1);
					} else {
						setFail(getFail()+1);
					}
					addCurrentRecord(1);
				}
			}
			totalSize = countWaitDatas();
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract Collection queryWaitDatas(int size) throws Exception;
	
	protected abstract int countWaitDatas() throws Exception;
	
	/**
	 * 实际业务处理方法
	 * @param record
	 * @return
	 */
	protected abstract DataBaseResult doProcess(Object record);
	
	protected Boolean finalWork(DbBatchTask task) {
		return true;
	}

	protected void destory() {
    	
    }
	
	protected Boolean initialization() {
    	return true;
    }
}
