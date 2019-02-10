package com.openjava.admin.batch.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author 何子右
 *
 */
public class BatchFileimportTaskDBParam extends RoDBQueryParam {
	private String eq_taskId;//任务ID --主键查询
	
	private String like_taskName;//任务名称 like ?
	private String eq_taskType;//任务类型 = ?
	private String eq_operator;//操作人账号 = ?
	private String eq_operatorId;//操作人ID = ?
	private String eq_operatorName;//操作人名称 = ?
	private String eq_procState;//处理状态（job.process.state） = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_createDate;//创建时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_createDate;//创建时间 >= ?
	private String eq_procWay;//处理方式 = ?
	
	public String getEq_taskId() {
		return eq_taskId;
	}
	public void setEq_taskId(String taskId) {
		this.eq_taskId = taskId;
	}
	
	public String getLike_taskName() {
		return like_taskName;
	}
	public void setLike_taskName(String taskName) {
		this.like_taskName = taskName;
	}
	public String getEq_taskType() {
		return eq_taskType;
	}
	public void setEq_taskType(String taskType) {
		this.eq_taskType = taskType;
	}
	public String getEq_operator() {
		return eq_operator;
	}
	public void setEq_operator(String operator) {
		this.eq_operator = operator;
	}
	public String getEq_operatorId() {
		return eq_operatorId;
	}
	public void setEq_operatorId(String operatorId) {
		this.eq_operatorId = operatorId;
	}
	public String getEq_operatorName() {
		return eq_operatorName;
	}
	public void setEq_operatorName(String operatorName) {
		this.eq_operatorName = operatorName;
	}
	public String getEq_procState() {
		return eq_procState;
	}
	public void setEq_procState(String procState) {
		this.eq_procState = procState;
	}
	public Date getLe_createDate() {
		return le_createDate;
	}
	public void setLe_createDate(Date createDate) {
		this.le_createDate = createDate;
	}
	public Date getGe_createDate() {
		return ge_createDate;
	}
	public void setGe_createDate(Date createDate) {
		this.ge_createDate = createDate;
	}
	public String getEq_procWay() {
		return eq_procWay;
	}
	public void setEq_procWay(String procWay) {
		this.eq_procWay = procWay;
	}
}