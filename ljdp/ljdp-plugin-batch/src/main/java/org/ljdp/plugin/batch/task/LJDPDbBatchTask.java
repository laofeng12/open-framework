package org.ljdp.plugin.batch.task;

import org.ljdp.component.strategy.DbBatchBusinessObject;
import org.ljdp.component.task.BODbBatchTask;
import org.ljdp.plugin.batch.pool.MemoryTaskPool;
import org.ljdp.plugin.batch.pool.TaskPoolManager;

public class LJDPDbBatchTask extends BODbBatchTask {

	private static final long serialVersionUID = 7759579783784430131L;

	public LJDPDbBatchTask(DbBatchBusinessObject bo) {
		super(bo);
	}
	
	@Override
	protected void destory() {
		if(TaskPoolManager.getBsPool().containsTask(getId())) {
			MemoryTaskPool.putTask(this);
			TaskPoolManager.getBsPool().removeTask(this);
		}
		super.destory();
	}
}
