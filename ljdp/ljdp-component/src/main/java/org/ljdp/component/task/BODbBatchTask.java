package org.ljdp.component.task;

import java.util.Collection;

import org.ljdp.component.result.DataBaseResult;
import org.ljdp.component.strategy.DbBatchBusinessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BODbBatchTask extends DbBatchTask {
	private static final long serialVersionUID = 6910988368390518123L;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected DbBatchBusinessObject bo;
	
	public BODbBatchTask(DbBatchBusinessObject bo) {
		super();
		this.bo = bo;
	}
	
	@Override
	protected void destory() {
		super.destory();
		bo.destory();
	}
	
	@Override
	protected Boolean initialization() {
		Boolean flag = super.initialization();
		if(flag) {
			flag = bo.initialization();
		}
		return flag;
	}
	
	@Override
	protected Boolean finalWork(DbBatchTask task) {
		Boolean flag = super.finalWork(task);
		if(flag) {
			flag = (Boolean)bo.finalWork(task);
		}
		return flag;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected Collection queryWaitDatas(int size) throws Exception {
		return (Collection)bo.queryWaitDatas(size);
	}

	@Override
	protected int countWaitDatas() throws Exception {
		Integer count = (Integer)bo.countWaitDatas();
		return count;
	}

	@Override
	protected DataBaseResult doProcess(Object record) {
		DataBaseResult result = bo.doProcess(record);
		return result;
	}

}
