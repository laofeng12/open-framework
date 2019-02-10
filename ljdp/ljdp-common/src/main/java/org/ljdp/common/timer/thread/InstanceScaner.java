package org.ljdp.common.timer.thread;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.ljdp.common.cache.MemoryCache;
import org.ljdp.common.timer.ScheduleMemoryPool;
import org.ljdp.common.timer.model.ScheduleInstance;
import org.ljdp.component.session.Request;
import org.ljdp.component.task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceScaner implements Runnable {
	private Logger log = LoggerFactory.getLogger(InstanceScaner.class);
	private ScheduledExecutorService scheduleServer = Executors.newScheduledThreadPool(20);
	private boolean run = true;
	private boolean beginOwnActive = false;
	private long activateInterval = 1 * 60L;//自我激活时间间隔
	private String status;//线程状态
	
	public void run() {
		try {
			log.info("Start...");
			setRun(true);
			while(isRun()) {
				synchronized (this) {
					status = "waiting";
					wait();
				}
				if(!isRun()) {
					break;
				}
				try {
					MemoryCache.lock();
					status = "running";
					Collection<ScheduleInstance> instances = ScheduleMemoryPool.allInstances();
					Iterator<ScheduleInstance> it = instances.iterator();
					while (it.hasNext()) {
						ScheduleInstance si = (ScheduleInstance) it.next();
						if(si.getStatus().equals(TaskStatus.WAIT)) {
							long delay = TimeUnit.MILLISECONDS.toSeconds(
									si.getRunTime().getTime() - System.currentTimeMillis());
							if(delay < 0) {
								delay = 0;
							}
							if(delay <= activateInterval) {
								Request request = si.getRequest();//默认使用当前实例参数
								if(request == null) {
									request = si.getTask().getRequest();//使用任务的参数
								}
								ScheduleThread st = new ScheduleThread(
										si.getId(), si.getTask().getBusinessObject(), request);
								si.setStatus(TaskStatus.PREPARED);
								scheduleServer.schedule(st, delay, TimeUnit.SECONDS);
							}
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
					log.error("实例扫描定时器异常", e);
				} finally {
					MemoryCache.unlock();
				}
				//除了由外部调用激活外，还自我定时激活
				if(!beginOwnActive) {
					scheduleServer.schedule(new TimingActivation(this), activateInterval, TimeUnit.SECONDS);
					beginOwnActive = true;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("实例扫描定时器异常退出", e);
		} finally {
			status = "stop";
			log.info("End...");
		}
	}
	
	public void shutdown() {
		setRun(false);
		synchronized (this) {
			notify();
		}
		scheduleServer.shutdown();
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public boolean isBeginOwnActive() {
		return beginOwnActive;
	}

	public void setBeginOwnActive(boolean beginOwnActive) {
		this.beginOwnActive = beginOwnActive;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
