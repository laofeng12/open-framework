package com.openjava.admin.job.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysJobWaitQueueDBParam extends RoDBQueryParam {
	private Long eq_queueNo;//队列序号 --主键查询
	
	private String eq_jobId;//任务编号 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_scheduleTime;//计划执行时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_scheduleTime;//计划执行时间 >= ?
	
	private Boolean null_scheduleTime;
	
	public Long getEq_queueNo() {
		return eq_queueNo;
	}
	public void setEq_queueNo(Long queueNo) {
		this.eq_queueNo = queueNo;
	}
	
	public String getEq_jobId() {
		return eq_jobId;
	}
	public void setEq_jobId(String jobId) {
		this.eq_jobId = jobId;
	}
	public Date getLe_scheduleTime() {
		return le_scheduleTime;
	}
	public void setLe_scheduleTime(Date scheduleTime) {
		this.le_scheduleTime = scheduleTime;
	}
	public Date getGe_scheduleTime() {
		return ge_scheduleTime;
	}
	public void setGe_scheduleTime(Date scheduleTime) {
		this.ge_scheduleTime = scheduleTime;
	}
	public Boolean getNull_scheduleTime() {
		return null_scheduleTime;
	}
	public void setNull_scheduleTime(Boolean null_scheduleTime) {
		this.null_scheduleTime = null_scheduleTime;
	}
}