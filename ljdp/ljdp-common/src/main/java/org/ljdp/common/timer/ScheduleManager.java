package org.ljdp.common.timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ljdp.common.timer.model.ScheduleInstance;
import org.ljdp.common.timer.model.ScheduleTask;
import org.ljdp.common.timer.model.ScheduleTaskInfo;
import org.ljdp.common.timer.model.TimerRule;
import org.ljdp.common.timer.thread.InstanceScaner;
import org.ljdp.common.timer.thread.TaskScaner;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.session.Request;
import org.ljdp.component.session.RequestParse;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.component.task.TaskStatus;

public class ScheduleManager {
	private static InstanceScaner iScaner;
	private static TaskScaner tScaner;
	
	static {
		tScaner = new TaskScaner();
		new Thread(tScaner).start();
		
		iScaner = new InstanceScaner();
		new Thread(iScaner).start();
	}
	
	public static synchronized void addTask(ScheduleTask task) {
		ScheduleMemoryPool.putTask(task);
		scanTask(task.getId());
	}
	
	public static synchronized void scanTask(String id) {
		tScaner.add(id);
	}

	public static synchronized BusinessObject getTaskBusinessObject(String taskId) {
		ScheduleTask task = ScheduleMemoryPool.getTask(taskId);
		return task.getBusinessObject();
	}
	
	/**
	 * 指定时间运行
	 * @param id
	 * @param name
	 * @param runTime
	 * @param bo
	 * @param params
	 * @throws Exception
	 */
	public static void schedule(String id, String name, Date runTime, BusinessObject bo, String params) throws Exception {
		ScheduleTask task = new ScheduleTask();
		if(StringUtils.isBlank(id)) {
			id = TimeSequence.getInstance().getSequence()+"";
		}
		task.setId(id);
		task.setName(name);
		task.setBeginTime(runTime);
		task.setBusinessObject(bo);
		task.setRequest(RequestParse.parse(params));
		
		addTask(task);
		scan();
	}
	
	public static void schedule(String name, Date runTime, BusinessObject bo, String params) throws Exception {
		schedule(null, name, runTime, bo, params);
	}
	
	/**
	 * 延时运行
	 * @param id
	 * @param name
	 * @param delaySecond
	 * @param bo
	 * @param params
	 * @throws Exception
	 */
	public static void schedule(String id, String name, int delaySecond, BusinessObject bo, String params) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, delaySecond);
		schedule(id, name, cal.getTime(), bo, params);
	}
	
	public static void schedule(String name, int delaySecond, BusinessObject bo, String params) throws Exception {
		schedule(null, name, delaySecond, bo, params);
	}

	public static void schedule(BusinessObject bo, String params) throws Exception{
		schedule(bo.getClass().getName(), 0, bo, params);
	}
	
	/**
	 * 自定义规则运行
	 * @param id
	 * @param name
	 * @param rule
	 * @param bo
	 * @param params
	 * @throws Exception
	 */
	public static void schedule(String id, String name, String rule, BusinessObject bo, String params) throws Exception {
		TimerRule timerRule = TimerRuleParse.parseString(rule);
		ScheduleTask task = new ScheduleTask();
		if(StringUtils.isBlank(id)) {
			id = TimeSequence.getInstance().getSequence()+"";
		}
		task.setId(id);
		task.setName(name);
		task.setRule(timerRule);
		task.setBusinessObject(bo);
		task.setRequest(RequestParse.parse(params));
		
		addTask(task);
		scan();
	}
	
	public static void schedule(String name, String rule, BusinessObject bo, String params) throws Exception {
		schedule(null, name, rule, bo, params);
	}
	
	/**
	 * 指定时间启动定时任务，如果任务存在，返回true
	 * @param id
	 * @param runTime
	 */
	public static synchronized boolean schedule(String id, Date runTime, Request request) {
		ScheduleTask task = ScheduleMemoryPool.getTask(id);
		if(task != null) {
			ScheduleInstance instance = new ScheduleInstance();
			instance.setId(TimeSequence.getInstance().getSequence(""));
			instance.setTaskId(id);
			instance.setRunTime(runTime);
			instance.setStatus(TaskStatus.WAIT);
			instance.setRequest(request);
			instance.setIsolate(true);
			ScheduleMemoryPool.putInstance(instance);
			scan();
			return true;
		}
		return false;
	}
	
	public static boolean schedule(String id, Date runTime) {
		return schedule(id, runTime, null);
	}
	
	/**
	 * delaySecond秒后启动定时任务
	 * @param id
	 * @param delaySecond
	 */
	public static boolean schedule(String id, int delaySecond, Request request) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, delaySecond);
		return schedule(id, cal.getTime(), request);
	}
	
	public static boolean schedule(String id, int delaySecond) {
		return schedule(id, delaySecond);
	}
	
	public static synchronized List<ScheduleTaskInfo> listTask(){
		ArrayList<ScheduleTask> list = new ArrayList<ScheduleTask>();
		list.addAll(ScheduleMemoryPool.allTasks());
		Collections.sort(list);
		
		ArrayList<ScheduleTaskInfo> res = new ArrayList<ScheduleTaskInfo>();
		for(int i = 0; i < list.size(); ++i) {
			ScheduleTask task = list.get(i);
			ScheduleTaskInfo taskinfo = new ScheduleTaskInfo();
			taskinfo.setId(task.getId());
			taskinfo.setName(task.getName());
			ScheduleInstance instance = ScheduleMemoryPool.lastInstance(task.getId());
			if(instance != null) {
				taskinfo.setRunTime(instance.getRunTime());
				taskinfo.setActualTime(instance.getActualTime());
				taskinfo.setFinishTime(instance.getFinishTime());
				taskinfo.setStatus(TaskStatus.translation(instance.getStatus()));
				taskinfo.setMessage(instance.getMessage());
			}
			res.add(taskinfo);
		}
		return res;
	}
	
	public static void scan() {
		synchronized (iScaner) {
			iScaner.notify();
		}
	}
	
	public static synchronized void shutdownAll() {
		tScaner.shutdown();
		iScaner.shutdown();
	}
	
	public static  InstanceScaner getIScaner() {
		return iScaner;
	}
	
	public static  TaskScaner getTaskScaner() {
		return tScaner;
	}
	
	public static synchronized void restartInstanceScaner() {
		iScaner.shutdown();
		iScaner = new InstanceScaner();
		new Thread(iScaner).start();
	}
	
	public static synchronized void restartTaskScaner() {
		tScaner.shutdown();
		tScaner = new TaskScaner();
		new Thread(tScaner).start();
	}
	
}
