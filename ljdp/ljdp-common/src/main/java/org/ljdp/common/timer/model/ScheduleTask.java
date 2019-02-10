package org.ljdp.common.timer.model;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.ljdp.common.timer.ScheduleMemoryPool;
import org.ljdp.component.session.Request;
import org.ljdp.component.strategy.BusinessObject;

public class ScheduleTask implements Comparable<ScheduleTask>{
	private String id;
	private String name;
	private Date beginTime;
	private TimerRule rule;
	private BusinessObject businessObject;
	private Request request;
	private boolean finish = false;
	private String sign;
	
	public ScheduleTask() {
		
	}
	public ScheduleTask(String name) {
		super();
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public TimerRule getRule() {
		return rule;
	}
	public void setRule(TimerRule rule) {
		this.rule = rule;
	}
	public BusinessObject getBusinessObject() {
		return businessObject;
	}
	public void setBusinessObject(BusinessObject businessObject) {
		this.businessObject = businessObject;
	}

	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
	public boolean isFinish() {
		return finish;
	}
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public int compareTo(ScheduleTask o) {
		int tsort = 1;
		if(!finish) {
			tsort = 0;
		}
		int osort = 1;
		if(!o.isFinish()) {
			osort = 0;
		}
		int compare = tsort - osort;
		if(compare == 0) {
			ScheduleInstance tsi = ScheduleMemoryPool.lastInstance(this.getId());
			ScheduleInstance osi = ScheduleMemoryPool.lastInstance(o.getId());
			if(tsi != null) {
				compare = tsi.compareTo(osi);
			}
		}
		return compare;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
