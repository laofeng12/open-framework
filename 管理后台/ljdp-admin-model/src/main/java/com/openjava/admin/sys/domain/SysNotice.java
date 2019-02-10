package com.openjava.admin.sys.domain;

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
 * @author heizyou
 *
 */
@ApiModel("通知")
@Entity
@Table(name = "TSYS_NOTICE")
public class SysNotice extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("通知id")
	private String nid;//通知id
	@ApiModelProperty("通知类型")
	private String ntype;//通知类型（notice.type）
	private String ntypeName;
	@ApiModelProperty("通知标题")
	private String title;//通知标题
	@ApiModelProperty("通知内容")
	private String content;//通知内容
	@ApiModelProperty("通知banner图")
	private String picBanner;//通知banner图
	@ApiModelProperty("创建人")
	private String createUser;//创建人
	@ApiModelProperty("创建时间")
	private Date createTime;//创建时间
	@ApiModelProperty("更新人员")
	private String updateUser;//更新人员
	@ApiModelProperty("更新时间")
	private Date updateTime;//更新时间
	@ApiModelProperty("通知状态")
	private String nstatus;//通知状态（notice.status）
	private String nstatusName;
	
	@Id
	@Column(name = "NID")
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	

	@Column(name = "NTYPE")
	public String getNtype() {
		return ntype;
	}
	public void setNtype(String ntype) {
		this.ntype = ntype;
	}
	
	@Transient
	public String getNtypeName() {
		return ntypeName;
	}
	public void setNtypeName(String ntypeName) {
		this.ntypeName = ntypeName;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

	@Column(name = "PIC_BANNER")
	public String getPicBanner() {
		return picBanner;
	}
	public void setPicBanner(String picBanner) {
		this.picBanner = picBanner;
	}
	

	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

	@Column(name = "UPDATE_USER")
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	

	@Column(name = "NSTATUS")
	public String getNstatus() {
		return nstatus;
	}
	public void setNstatus(String nstatus) {
		this.nstatus = nstatus;
	}
	
	@Transient
	public String getNstatusName() {
		return nstatusName;
	}
	public void setNstatusName(String nstatusName) {
		this.nstatusName = nstatusName;
	}
}