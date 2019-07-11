package org.ljdp.common.timer.model;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.ljdp.common.timer.ScheduleMemoryPool;
import org.ljdp.common.timer.thread.ScheduleThread;
import org.ljdp.component.result.BatchResult;
import org.ljdp.component.session.Request;
import org.ljdp.component.task.TaskCursor;
import org.ljdp.component.task.TaskStatus;

public class ScheduleInstance implements Comparable<ScheduleInstance>{
	private String id;
	private String taskId;
	private String status;
	private Date runTime;//计划运行时间
	private Date actualTime;//实际运行时间
	private Date finishTime;//完成时间
	private String message;
	private TaskCursor cursor;//任务运行情况
	private Object resultObj;
	private Request request;
	private boolean isolate = false;//隔离实例，运行完后不触发任务扫描，不重复执行定时规则
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getRunTime() {
		return runTime;
	}
	public void setRunTime(Date runtime) {
		this.runTime = runtime;
	}
	
	public ScheduleTask getTask() {
		ScheduleTask task = ScheduleMemoryPool.getTask(taskId);
		return task;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ScheduleThread getThread() {
		ScheduleThread thread = ScheduleMemoryPool.getThread(getId());
		return thread;
	}
	public Date getActualTime() {
		return actualTime;
	}
	public void setActualTime(Date actualTime) {
		this.actualTime = actualTime;
	}
	public TaskCursor getCursor() {
		return cursor;
	}
	public void setCursor(TaskCursor cursor) {
		this.cursor = cursor;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Object getResultObj() {
		return resultObj;
	}
	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
	public BatchResult getResult() {
		return (BatchResult)getResultObj();
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public int compareTo(ScheduleInstance o) {
		if(o == null) {
			return 1;
		}
		int sort = TaskStatus.compare(this.getStatus(), o.getStatus());
		if(sort == 0) {
			sort = this.getRunTime().compareTo(o.getRunTime());
			if(TaskStatus.isFinish(this.getStatus()) &&
					TaskStatus.isFinish(o.getStatus())) {
				sort *= -1;
			}
		}
		return sort;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public boolean isIsolate() {
		return isolate;
	}
	public void setIsolate(boolean isolate) {
		this.isolate = isolate;
	}

}
