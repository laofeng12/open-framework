package com.openjava.admin.job.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author 子右
 *
 */
public class SysJobScheduleDBParam extends RoDBQueryParam {
	private String eq_jobId;//任务编号 --主键查询
	
	private String like_jobName;//任务名称 like ?
	private String eq_jobClass;//类名 = ?
	private String eq_jobMethod;//方法 = ?
	
	public String getEq_jobId() {
		return eq_jobId;
	}
	public void setEq_jobId(String jobId) {
		this.eq_jobId = jobId;
	}
	
	public String getLike_jobName() {
		return like_jobName;
	}
	public void setLike_jobName(String jobName) {
		this.like_jobName = jobName;
	}
	public String getEq_jobClass() {
		return eq_jobClass;
	}
	public void setEq_jobClass(String jobClass) {
		this.eq_jobClass = jobClass;
	}
	public String getEq_jobMethod() {
		return eq_jobMethod;
	}
	public void setEq_jobMethod(String jobMethod) {
		this.eq_jobMethod = jobMethod;
	}
}