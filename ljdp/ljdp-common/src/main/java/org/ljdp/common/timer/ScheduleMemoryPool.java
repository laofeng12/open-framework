package org.ljdp.common.timer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheRegion;
import org.ljdp.common.cache.CacheType;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.timer.model.ScheduleInstance;
import org.ljdp.common.timer.model.ScheduleTask;
import org.ljdp.common.timer.thread.ScheduleThread;
import org.ljdp.component.task.TaskStatus;

public class ScheduleMemoryPool {
	private static final String KEY_INSTANCE = "ljdp.common.timer.scheduleinstance";
	private static final String KEY_TASK = "ljdp.common.timer.scheduletask";
	private static final String KEY_THREAD = "ljdp.common.timer.schedulethread";
	
	static {
		//初始化定时任务运行实例池
		MemoryCache.initCache(KEY_INSTANCE, CacheType.PERMANENT);
		//初始化定时任务池
		MemoryCache.initCache(KEY_TASK, CacheType.PERMANENT);
		//初始化定时任务运行实例的线程池
		MemoryCache.initCache(KEY_THREAD, CacheType.PERMANENT);
		
		//不允许自动清理
//		MemoryCache.config(KEY_INSTANCE, CacheParam.AUTO_CLEAR, new Boolean(false));
//		MemoryCache.config(KEY_TASK, CacheParam.AUTO_CLEAR, new Boolean(false));
//		MemoryCache.config(KEY_THREAD, CacheParam.AUTO_CLEAR, new Boolean(false));
	}
	
	public static synchronized void putInstance(ScheduleInstance instance) {
		MemoryCache.putData(KEY_INSTANCE, instance.getId(), instance);
	}
	
	public static synchronized ScheduleInstance getInstance(String id) {
		return (ScheduleInstance)MemoryCache.getData(KEY_INSTANCE, id);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Collection<ScheduleInstance> allInstances(){
		CacheRegion cr = MemoryCache.getCache(KEY_INSTANCE);
		return cr.datas();
	}
	
	public static synchronized void removeInstance(String id) {
		MemoryCache.removeData(KEY_INSTANCE, id);
	}
	
	public static synchronized void clearInstance(String taskId) {
		org.ljdp.common.cache.MemoryCache.lock();
		try {
			List<String> waitRemoveList = new ArrayList<String>();
			Collection<ScheduleInstance> instances = allInstances();
			Iterator<ScheduleInstance> iter = instances.iterator();
			while(iter.hasNext()) {
				ScheduleInstance si = iter.next();
				if(si.getTaskId().equals(taskId)) {
					if(si.getStatus().equals(TaskStatus.SUCCESS) 
							|| si.getStatus().equals(TaskStatus.FAILURE)) {
						waitRemoveList.add(si.getId());
					}
				}
			}
			for (int i = 0; i < waitRemoveList.size(); i++) {
				String  iid = waitRemoveList.get(i);
				removeInstance(iid);
			}
		} finally {
			org.ljdp.common.cache.MemoryCache.unlock();
		}
	}
	
	public static synchronized void putTask(ScheduleTask task) {
		MemoryCache.putData(KEY_TASK, task.getId(), task);
	}
	
	public static synchronized ScheduleTask getTask(String id) {
		return (ScheduleTask)MemoryCache.getData(KEY_TASK, id);
	}
	
	public static synchronized void removeTask(String id) {
		MemoryCache.removeData(KEY_TASK, id);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Collection<ScheduleTask> allTasks(){
		CacheRegion cr = MemoryCache.getCache(KEY_TASK);
		return cr.datas();
	}
	
	public static synchronized void putThread(ScheduleThread thread) {
		MemoryCache.putData(KEY_THREAD, thread.getId(), thread);
	}
	
	public static synchronized ScheduleThread getThread(String id) {
		return (ScheduleThread)MemoryCache.getData(KEY_THREAD, id);
	}
	
	public static synchronized void removeThread(String id) {
		MemoryCache.removeData(KEY_THREAD, id);
	}
	
	public static synchronized ArrayList<ScheduleInstance> allInstances(String taskId) {
		org.ljdp.common.cache.MemoryCache.lock();
		try {
			ArrayList<ScheduleInstance> list = new ArrayList<ScheduleInstance>();
			Iterator<ScheduleInstance> iter = allInstances().iterator();
			while(iter.hasNext()) {
				ScheduleInstance si = iter.next();
				if(si.getTaskId().equals(taskId)) {
					list.add(si);
				}
			}
			Collections.sort(list);
			return list;
		} finally {
			org.ljdp.common.cache.MemoryCache.unlock();
		}
	}
	
	public static synchronized ScheduleInstance lastInstance(String taskId) {
		ArrayList<ScheduleInstance> list = allInstances(taskId);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
