package org.ljdp.plugin.batch.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheRegion;
import org.ljdp.common.cache.CacheType;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.plugin.batch.model.Batch;
import org.ljdp.plugin.batch.model.TaskInfoVO;

public class MemoryTaskPool {
	
	static {
		//任务完成后在内存保存30分钟
		MemoryCache.initCache(Batch.CACHE_ID, CacheType.LIFE);
		MemoryCache.config(Batch.CACHE_ID, CacheParam.TIME_LIFE_MINUTE, 30);
	}

	@SuppressWarnings("rawtypes")
	public static ArrayList<TaskInfoVO> queryByUser(String userId) {
		ArrayList<TaskInfoVO> list = new ArrayList<TaskInfoVO>();
		CacheRegion pm = MemoryCache.getCache(Batch.CACHE_ID);
		Collection memdatas = pm.datas();
		Iterator it = memdatas.iterator();
		while(it.hasNext()) {
			BaseBatchTask task = (BaseBatchTask)it.next();
			DBAccessUser dbUser = (DBAccessUser)task.getUser();
			if(dbUser == null && StringUtils.isEmpty(userId)) {
				TaskInfoVO vo = new TaskInfoVO(task.getId(), task.getName());
				vo.setType(task.getType());
				vo.setProcessWay(task.getWay());
				list.add(vo);
			} else if(StringUtils.isEmpty(userId) || dbUser.getId().equals(userId)) {
				TaskInfoVO vo = new TaskInfoVO(task.getId(), task.getName());
				vo.setType(task.getType());
				vo.setOperatorId(dbUser.getId());
				vo.setOperatorAccount(dbUser.getAccount());
				vo.setOperatorName(dbUser.getName());
				vo.setProcessWay(task.getWay());
				list.add(vo);
			}
		}
		return list;
	}

	public static void putTask(BaseBatchTask task) {
		MemoryCache.putData(Batch.CACHE_ID, task.getId(), task);
	}

	public static BaseBatchTask getTask(String key) {
		return (BaseBatchTask)MemoryCache.getData(Batch.CACHE_ID, key);
	}
	
	public static void removeTask(String key) {
		MemoryCache.removeData(Batch.CACHE_ID, key);
	}
	
	public static void clearAllTask() {
		MemoryCache.getEhCache(Batch.CACHE_ID).removeAll();
	}
}
