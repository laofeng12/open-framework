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
@ApiModel("任务执行日志")
@Entity
@Table(name = "SYS_JOB_LOG")
public class SysJobLog extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("日志ID")
	private Long logSeq;
	
	@ApiModelProperty("任务编号")
	private String jobId;
	
	@ApiModelProperty("参数")
	private String jobParams;
	
	@ApiModelProperty("开始时间")
	private Date startTime;
	
	@ApiModelProperty("完成时间")
	private Date finishTime;
	
	@ApiModelProperty("状态(schedule.job.status) 0：待执行 1：运行中 2：成功 3：失败")
	private Long status;
	@ApiModelProperty("状态(schedule.job.status) 0：待执行 1：运行中 2：成功 3：失败名称")
	private String statusName;
	
	@ApiModelProperty("错误日志")
	private String errorLog;
	
	
	@Id
	@Column(name = "LOG_SEQ")
	public Long getLogSeq() {
		return logSeq;
	}
	public void setLogSeq(Long logSeq) {
		this.logSeq = logSeq;
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
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	

	@Column(name = "STATUS")
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	
	@Transient
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Column(name = "ERROR_LOG")
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	
}