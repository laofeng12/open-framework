package com.openjava.admin.job.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysJobLogDBParam extends RoDBQueryParam {
	private Long eq_logSeq;//日志ID --主键查询
	
	private String eq_jobId;//任务编号 = ?
	private Long eq_status;//状态(schedule.job.status) 0：待执行 1：运行中 2：成功 3：失败 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_startTime;//开始时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_startTime;//开始时间 >= ?
	
	public Long getEq_logSeq() {
		return eq_logSeq;
	}
	public void setEq_logSeq(Long logSeq) {
		this.eq_logSeq = logSeq;
	}
	
	public String getEq_jobId() {
		return eq_jobId;
	}
	public void setEq_jobId(String jobId) {
		this.eq_jobId = jobId;
	}
	public Long getEq_status() {
		return eq_status;
	}
	public void setEq_status(Long status) {
		this.eq_status = status;
	}
	public Date getLe_startTime() {
		return le_startTime;
	}
	public void setLe_startTime(Date startTime) {
		this.le_startTime = startTime;
	}
	public Date getGe_startTime() {
		return ge_startTime;
	}
	public void setGe_startTime(Date startTime) {
		this.ge_startTime = startTime;
	}
}