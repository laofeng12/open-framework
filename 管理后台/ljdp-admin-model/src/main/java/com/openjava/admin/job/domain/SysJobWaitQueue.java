package com.openjava.admin.job.domain;

import java.util.Date;

import javax.persistence.*;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author hzy
 *
 */
@ApiModel("待执行任务队列")
@Entity
@Table(name = "SYS_JOB_WAIT_QUEUE")
public class SysJobWaitQueue extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("队列序号")
	private Long queueNo;
	
	@ApiModelProperty("任务编号")
	private String jobId;
	
	@ApiModelProperty("参数")
	private String jobParams;
	
	@ApiModelProperty("计划执行时间")
	private Date scheduleTime;
	
	
	@Id
	@Column(name = "QUEUE_NO")
	public Long getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(Long queueNo) {
		this.queueNo = queueNo;
	}
	

	@Column(name = "JOB_ID")
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	

	@Column(name = "JOB_PARAMS")
	public String getJobParams() {
		return jobParams;
	}
	public void setJobParams(String jobParams) {
		this.jobParams = jobParams;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SCHEDULE_TIME")
	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	
}