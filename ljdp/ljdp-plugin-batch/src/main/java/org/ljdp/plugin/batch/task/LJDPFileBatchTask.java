package org.ljdp.plugin.batch.task;

import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.module.filetask.BOFileBatchTask;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.pool.MemoryTaskPool;
import org.ljdp.plugin.batch.pool.TaskPoolManager;

/**
 * LJDP架构封装的文件数据批量处理任务。
 * 实现了任务结束后释放占用的线程池，
 * 并把任务移至一个临时的内存池中保存(可选，根据endInMemoryTemp)，便于快速查询
 * 
 * @author hzy
 *
 */
public class LJDPFileBatchTask extends BOFileBatchTask {
	private static final long serialVersionUID = 385027441905010674L;
	private boolean endInMemoryTemp = true;

	public LJDPFileBatchTask(FileBusinessObject bo) {
		super(bo);
	}

	@Override
	protected void destory() {
		if(TaskPoolManager.getFgPool().containsTask(getId())) {
			if(getWay().equals(BatchFileDic.PW_FB_F)) {
				if(wholeTaskSuccess) {
					getCursor().resset();
				}
			}
			//如果任务没成功完成，需记录到临时池查看
			if(isEndInMemoryTemp() || !wholeTaskSuccess) {
				MemoryTaskPool.putTask(this);
			}
			TaskPoolManager.getFgPool().removeTask(this);
		}
		super.destory();
	}

	public boolean isEndInMemoryTemp() {
		return endInMemoryTemp;
	}

	/**
	 * 结束后是否继续在内存保留一段时间，默认true，保留30分
	 * @param endInMemoryTemp
	 */
	public void setEndInMemoryTemp(boolean endInMemoryTemp) {
		this.endInMemoryTemp = endInMemoryTemp;
	}

}
