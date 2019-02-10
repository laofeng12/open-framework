package org.ljdp.plugin.batch.pool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.plugin.batch.model.TaskInfoVO;

public class ThreadTaskPool {
	private ExecutorService executor;

	private ConcurrentHashMap<String, BaseBatchTask> taskMap;
	
	public ThreadTaskPool(int size) {
		executor = Executors.newFixedThreadPool(size);
		taskMap = new ConcurrentHashMap<String, BaseBatchTask>();
	}

	public void addTask(BaseBatchTask task) {
		synchronized (this) {
			if(!containsTask(task.getId())) {
				taskMap.put(task.getId(), task);
				executor.execute(task);
			}
		}
	}
	
	public BaseBatchTask removeTask(BaseBatchTask task) {
		synchronized (this) {
			return taskMap.remove(task.getId());
		}
	}
	
	public BaseBatchTask removeTask(String id) {
		synchronized (this) {
			return taskMap.remove(id);
		}
	}
	
	public boolean containsTask(String id) {
		return taskMap.containsKey(id);
	}
	
	public void shutdownAll() {
		executor.shutdown();
		taskMap.clear();
	}
	
	public BaseBatchTask getTaskByID(String id) {
		return taskMap.get(id);
	}
	
	public ArrayList<TaskInfoVO> queryByUser(String userId){
		synchronized (this) {
			Iterator<BaseBatchTask> it = taskMap.values().iterator();
			ArrayList<TaskInfoVO> list = new ArrayList<TaskInfoVO>();
			while(it.hasNext()) {
				BaseBatchTask task = it.next();
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
	}
}
