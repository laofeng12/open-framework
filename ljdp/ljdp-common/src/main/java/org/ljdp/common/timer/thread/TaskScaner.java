package org.ljdp.common.timer.thread;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.ljdp.common.cache.MemoryCache;
import org.ljdp.common.timer.ScheduleManager;
import org.ljdp.common.timer.ScheduleMemoryPool;
import org.ljdp.common.timer.TimerRuleParse;
import org.ljdp.common.timer.model.ScheduleInstance;
import org.ljdp.common.timer.model.ScheduleTask;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.task.TaskStatus;
import org.ljdp.util.DateFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 扫描定时任务，计算任务下次运行的时间
 * @author hzy
 *
 */
public class TaskScaner implements Runnable{
	private Logger log = LoggerFactory.getLogger(TaskScaner.class);
	private boolean run = true;
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private String status;//线程状态
	
	private String scan(String taskId) {
		ScheduleTask task = ScheduleMemoryPool.getTask(taskId);
		if(task == null || task.isFinish()) {
			return null;
		}
		if(task.getSign() != null && task.getSign().equals("stop")) {
			return null;
		}
		Date nextRunTime = null;
		Date lastFinishTime = lastFinishTime(taskId);
		if(task.getRule() != null) {
			try {
				nextRunTime = TimerRuleParse.parseNextTime(task.getRule(), lastFinishTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(nextRunTime == null) {
			if( lastFinishTime == null ) {
				//第一次运行
				nextRunTime = task.getBeginTime();
			}
		}
		if(nextRunTime == null) {
			return null;
		}
		task.setBeginTime(nextRunTime);
		
		log.info(task.getId()+"|"+task.getName()+
				"|LAST-TIME:"+DateFormater.formatDate(lastFinishTime, "yyyy-MM-dd HH:mm:ss")+
				"|NEXT-TIME:"+DateFormater.formatDate(nextRunTime, "yyyy-MM-dd HH:mm:ss"));
		
		ScheduleMemoryPool.clearInstance(taskId);
		ScheduleInstance instance = new ScheduleInstance();
		instance.setId(TimeSequence.getInstance().getSequence(""));
		instance.setTaskId(taskId);
		instance.setRunTime(nextRunTime);
		instance.setStatus(TaskStatus.WAIT);
		ScheduleMemoryPool.putInstance(instance);
		return instance.getId();
	}

	public void run() {
		try {
			log.info("Start...");
			setRun(true);
			while(isRun()) {
				String taskId = null;
				try {
					status = "queueing";
					taskId = queue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error("任务队列获取异常",e);
				}
				if(!isRun()) {
					break;
				}
				if(taskId != null) {
					status = "running";
					boolean exist = existNotRunInstance(taskId);
					if(!exist) {
						String instanceId = scan(taskId);
						if(instanceId == null) {
							ScheduleTask task = ScheduleMemoryPool.getTask(taskId);
							if(task != null) {
								task.setFinish(true);
							}
						} else {
							ScheduleManager.scan();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("定时任务线程异常", e);
		} finally {
			status = "stop";
			log.info("end...");
		}
	}

	private boolean existNotRunInstance(String taskId) {
		boolean exist = false;
		try {
			MemoryCache.lock();
			Collection<ScheduleInstance> instances = ScheduleMemoryPool.allInstances();
			Iterator<ScheduleInstance> iter = instances.iterator();
			while(iter.hasNext()) {
				ScheduleInstance si = iter.next();
				if(si.getTaskId().equals(taskId)) {
					if(si.getStatus().equals(TaskStatus.WAIT) 
							|| si.getStatus().equals(TaskStatus.PREPARED)) {
						exist = true;
						break;
					}
				}
			}
		} finally {
			MemoryCache.unlock();
		}
		return exist;
	}
	
	private Date lastFinishTime(String taskId) {
		Date time = null;
		try {
			MemoryCache.lock();
			Collection<ScheduleInstance> instances = ScheduleMemoryPool.allInstances();
			Iterator<ScheduleInstance> iter = instances.iterator();
			while(iter.hasNext()) {
				ScheduleInstance si = iter.next();
				if(si.getTaskId().equals(taskId)) {
					if(si.getFinishTime() != null) {
						if(time == null) {
							time = si.getFinishTime();
						} else if(time.before(si.getFinishTime())) {
							time = si.getFinishTime();
						}
					}
				}
			}
		} finally {
			MemoryCache.unlock();
		}
		return time;
	}
	
	public void add(String taskId) {
		queue.add(taskId);
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	public void shutdown() {
		setRun(false);
		queue.add("END");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
