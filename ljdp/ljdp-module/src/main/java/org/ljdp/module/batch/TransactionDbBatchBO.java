package org.ljdp.module.batch;

import org.ljdp.component.exception.BusinessException;
import org.ljdp.component.namespace.NameNotFoundException;
import org.ljdp.component.result.DataBaseResult;
import org.ljdp.component.result.GeneralDbResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.AbstractDbBatchBO;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.service.GeneralService;
import org.ljdp.core.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionDbBatchBO extends AbstractDbBatchBO {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected GeneralService gs;

	@Override
	public Boolean initialization() {
		try {
			gs = ServiceFactory.buildGeneral();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public DataBaseResult doProcess(Object record) {
		DataBaseResult res = new GeneralDbResult();
		try {
			res = (DataBaseResult)gs.doTransaction(new BusinessObject() {
				
				public Object doBusiness(Object... params) throws Exception {
					DataBaseResult result = doProcessRecord(params[0]);
					if(result.isSuccess()) {
						onProcessSuccess(params[0]);
					} else {
						if(result.isRollback()) {//需要回滚操作
							throw new BusinessException(result.getMsg());//使用业务异常，不打印异常栈
						}
						onProcessFail(params[0], result);
					}
					onProcessComplete(params[0], result);
					return result;
				}
				
			}, new Object[] {record});
		} catch(BusinessException e) {
			//业务失败
			log.info("业务操作失败："+e.getMessage());
			res.setSuccess(false);
			res.setMsg(e.getMessage());
			processException(record, res);
		} catch (Exception e) {
			log.error("后台批任务处理失败", e);
			res.setSuccess(false);
			Throwable t = e;
			while(t.getCause() != null) {
				t = t.getCause();
			}
			res.setMsg(t.getMessage());
			if(res.getMsg() != null) {
				//数据库异常翻译
				if(res.getMsg().indexOf("ORA-00001") >= 0) {//违反唯一约束条件
					res.setMsg("数据已存在，请检查是否重复导入");
				}
			}
			processException(record, res);
		}
		if(res.getDelay() > 0) {
			try {
				Thread.sleep(res.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	private void processException(Object record, DataBaseResult res) {
		try {
			onProcessFail(record, res);
			onProcessComplete(record, res);
		} catch (Exception e2) {
			res.rollBack();//更新数据库中的状态失败，则设置回滚标志，通知内存中的状态也不更新
			log.error("更新批任务结果异常", e2);
		}
	}

	protected abstract DataBaseResult doProcessRecord(Object record);
	
	protected void onProcessSuccess(Object record) throws Exception {
		
	}
	
	protected void onProcessFail(Object record, Result result) throws Exception {
		
	}
	
	protected void onProcessComplete(Object record, Result result) throws Exception {
		
	}

}
