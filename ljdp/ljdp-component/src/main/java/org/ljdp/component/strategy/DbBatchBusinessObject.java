package org.ljdp.component.strategy;

import java.util.Collection;

import org.ljdp.component.result.DataBaseResult;
import org.ljdp.component.task.DbBatchTask;

/**
 * 
 * 数据库批处理业务对象
 * @author Administrator
 *
 */
public interface DbBatchBusinessObject {
	
	public DataBaseResult doProcess(Object record);
	
	@SuppressWarnings("rawtypes")
	public Collection queryWaitDatas(int size) throws Exception;
	
	public int countWaitDatas() throws Exception;

	public void destory();

	public Boolean initialization();

	/**
	 * 所有子任务完成后的最后处理工作，返回整个任务是否成功完成，
	 * 只要有一个子任务失败，则应该返回false;
	 * @return
	 */
	public Boolean finalWork(DbBatchTask task);
	
}
