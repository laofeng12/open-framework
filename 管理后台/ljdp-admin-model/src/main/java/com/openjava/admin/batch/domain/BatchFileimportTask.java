package com.openjava.admin.batch.domain;

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
 * @author 何子右
 *
 */
@ApiModel("批处理任务")
@Entity
@Table(name = "BATCH_FILEIMPORT_TASK")
public class BatchFileimportTask extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("任务ID")
	private String taskId;
	
	@ApiModelProperty("任务名称")
	private String taskName;
	
	@ApiModelProperty("任务类型")
	private String taskType;
	
	@ApiModelProperty("操作类型")
	private String operType;
	
	@ApiModelProperty("操作人账号")
	private String operator;
	
	@ApiModelProperty("操作人ID")
	private String operatorId;
	
	@ApiModelProperty("操作人名称")
	private String operatorName;
	
	@ApiModelProperty("业务对象")
	private String bsBo;
	
	@ApiModelProperty("总数")
	private Long totalRec;
	
	@ApiModelProperty("成功数")
	private Long succRec;
	
	@ApiModelProperty("失败数")
	private Long failRec;
	
	@ApiModelProperty("失败日志")
	private String failLog;
	
	@ApiModelProperty("处理状态（job.process.state）")
	private String procState;
	@ApiModelProperty("处理状态（job.process.state）名称")
	private String procStateName;
	
	@ApiModelProperty("处理方式")
	private String procWay;
	@ApiModelProperty("处理方式名称")
	private String procWayName;
	
	@ApiModelProperty("消耗时间（秒）")
	private Long costTime;
	
	@ApiModelProperty("创建时间")
	private Date createDate;
	
	@ApiModelProperty("处理服务器IP")
	private String serverIp;
	
	@ApiModelProperty("开始执行时间")
	private Date beginTime;
	
	@ApiModelProperty("完成执行时间")
	private Date finishTime;
	
	@ApiModelProperty("处理文件")
	private String proceFileName;
	
	
	@Id
	@Column(name = "TASK_ID")
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	

	@Column(name = "TASK_NAME")
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	

	@Column(name = "TASK_TYPE")
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	

	@Column(name = "OPER_TYPE")
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	

	@Column(name = "OPERATOR")
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	

	@Column(name = "OPERATOR_ID")
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	

	@Column(name = "OPERATOR_NAME")
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	

	@Column(name = "BS_BO")
	public String getBsBo() {
		return bsBo;
	}
	public void setBsBo(String bsBo) {
		this.bsBo = bsBo;
	}
	

	@Column(name = "TOTAL_REC")
	public Long getTotalRec() {
		return totalRec;
	}
	public void setTotalRec(Long totalRec) {
		this.totalRec = totalRec;
	}
	

	@Column(name = "SUCC_REC")
	public Long getSuccRec() {
		return succRec;
	}
	public void setSuccRec(Long succRec) {
		this.succRec = succRec;
	}
	

	@Column(name = "FAIL_REC")
	public Long getFailRec() {
		return failRec;
	}
	public void setFailRec(Long failRec) {
		this.failRec = failRec;
	}
	

	@Column(name = "FAIL_LOG")
	public String getFailLog() {
		return failLog;
	}
	public void setFailLog(String failLog) {
		this.failLog = failLog;
	}
	

	@Column(name = "PROC_STATE")
	public String getProcState() {
		return procState;
	}
	public void setProcState(String procState) {
		this.procState = procState;
	}
	
	@Transient
	public String getProcStateName() {
		return procStateName;
	}
	public void setProcStateName(String procStateName) {
		this.procStateName = procStateName;
	}

	@Column(name = "PROC_WAY")
	public String getProcWay() {
		return procWay;
	}
	public void setProcWay(String procWay) {
		this.procWay = procWay;
	}
	
	@Transient
	public String getProcWayName() {
		return procWayName;
	}
	public void setProcWayName(String procWayName) {
		this.procWayName = procWayName;
	}

	@Column(name = "COST_TIME")
	public Long getCostTime() {
		return costTime;
	}
	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	

	@Column(name = "SERVER_IP")
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIME")
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
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
	

	@Column(name = "PROCE_FILE_NAME")
	public String getProceFileName() {
		return proceFileName;
	}
	public void setProceFileName(String proceFileName) {
		this.proceFileName = proceFileName;
	}
	
}