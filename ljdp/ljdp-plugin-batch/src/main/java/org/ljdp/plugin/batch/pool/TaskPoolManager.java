package org.ljdp.plugin.batch.pool;

import org.apache.commons.beanutils.BeanUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.strategy.DbBatchBusinessObject;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.model.TaskInfoVO;
import org.ljdp.plugin.batch.task.LJDPDbBatchTask;


public class TaskPoolManager {
	private final static ThreadTaskPool fgPool;
	private final static ThreadTaskPool bsPool;
	
	static {
		ConfigFile cfg = Env.current().getConfigFile();
		String fgCfg = cfg.getValue("batch.fg.threadPool.size", "1");
		String bsCfg = cfg.getValue("batch.bs.threadPool.size", "1");
		int fgSize = 1, bsSize = 1;
		try {
			fgSize = Integer.parseInt(fgCfg);
			bsSize = Integer.parseInt(bsCfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		fgPool = new ThreadTaskPool(fgSize);
		bsPool = new ThreadTaskPool(bsSize);
	}

	/**
	 * 获取前台任务线程池
	 * @return
	 */
	public static ThreadTaskPool getFgPool() {
		return fgPool;
	}

	public static ThreadTaskPool getBsPool() {
		return bsPool;
	}
	
	public static void addBsBatchTask(TaskInfoVO task) throws Exception{
		DbBatchBusinessObject bo = (DbBatchBusinessObject)
				SpringContextManager.getBean(task.getBsBusinessObject());
		if(bo == null) {
			throw new Exception("找不到业务对象:"+task.getBsBusinessObject());
		}
		
		DBAccessUser user = new DBAccessUser();
		user.setId(task.getOperatorId());
		user.setAccount(task.getOperatorAccount());
		user.setName(task.getOperatorName());
		try {
			BeanUtils.setProperty(bo, "user", user);
			BeanUtils.setProperty(bo, "operType", task.getOperType());
			BeanUtils.setProperty(bo, "batchID", task.getId());
		} catch (Exception e) {
			throw new Exception("设置业务对象参数失败");
		}
		LJDPDbBatchTask batch = new LJDPDbBatchTask(bo);
		batch.setId(task.getId());
		batch.setName(task.getName());
		batch.setType(task.getType());
		batch.setUser(user);
		batch.setWay(BatchFileDic.PW_FB_B);
		batch.setTotalRecords(task.getTotalNum());
		batch.setOk(task.getSuccessNum());
		batch.setFail(task.getFailNum());
		batch.setAutoBatchSize(true);
		if(bsPool.containsTask(batch.getId())) {
			throw new Exception("任务管理中已经存在和此任务相同ID的任务处理中");
		}
		bsPool.addTask(batch);
		MemoryTaskPool.removeTask(batch.getId());
	}
	
}
