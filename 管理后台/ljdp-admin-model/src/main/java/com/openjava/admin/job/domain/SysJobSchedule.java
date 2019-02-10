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
 * @author 子右
 *
 */
@ApiModel("计划任务")
@Entity
@Table(name = "SYS_JOB_SCHEDULE")
public class SysJobSchedule extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("任务编号")
	private String jobId;
	
	@ApiModelProperty("任务名称")
	private String jobName;
	
	@ApiModelProperty("类名")
	private String jobClass;
	
	@ApiModelProperty("方法")
	private String jobMethod;
	
	@ApiModelProperty("最近一次运行时间")
	private Date lastStartTime;
	
	@ApiModelProperty("最近一次结束时间")
	private Date lastEndTime;
	
	
	@Id
	@Column(name = "JOB_ID")
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	

	@Column(name = "JOB_NAME")
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	

	@Column(name = "JOB_CLASS")
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	

	@Column(name = "JOB_METHOD")
	public String getJobMethod() {
		return jobMethod;
	}
	public void setJobMethod(String jobMethod) {
		this.jobMethod = jobMethod;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_START_TIME")
	public Date getLastStartTime() {
		return lastStartTime;
	}
	public void setLastStartTime(Date lastStartTime) {
		this.lastStartTime = lastStartTime;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_END_TIME")
	public Date getLastEndTime() {
		return lastEndTime;
	}
	public void setLastEndTime(Date lastEndTime) {
		this.lastEndTime = lastEndTime;
	}
	
}