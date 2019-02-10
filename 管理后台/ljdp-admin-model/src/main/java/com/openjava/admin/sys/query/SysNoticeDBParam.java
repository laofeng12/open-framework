package com.openjava.admin.sys.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author heizyou
 *
 */
public class SysNoticeDBParam extends RoDBQueryParam {
	private String eq_nid;//通知id --主键查询
	
	private String eq_ntype;//通知类型（notice.type） = ?
	private String like_title;//通知标题 like ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_createTime;//创建时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_createTime;//创建时间 >= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_updateTime;//更新时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_updateTime;//更新时间 >= ?
	private String eq_nstatus;//通知状态（notice.status） = ?
	
	public String getEq_nid() {
		return eq_nid;
	}
	public void setEq_nid(String nid) {
		this.eq_nid = nid;
	}
	
	public String getEq_ntype() {
		return eq_ntype;
	}
	public void setEq_ntype(String ntype) {
		this.eq_ntype = ntype;
	}
	public String getLike_title() {
		return like_title;
	}
	public void setLike_title(String title) {
		this.like_title = title;
	}
	public Date getLe_createTime() {
		return le_createTime;
	}
	public void setLe_createTime(Date createTime) {
		this.le_createTime = createTime;
	}
	public Date getGe_createTime() {
		return ge_createTime;
	}
	public void setGe_createTime(Date createTime) {
		this.ge_createTime = createTime;
	}
	public Date getLe_updateTime() {
		return le_updateTime;
	}
	public void setLe_updateTime(Date updateTime) {
		this.le_updateTime = updateTime;
	}
	public Date getGe_updateTime() {
		return ge_updateTime;
	}
	public void setGe_updateTime(Date updateTime) {
		this.ge_updateTime = updateTime;
	}
	public String getEq_nstatus() {
		return eq_nstatus;
	}
	public void setEq_nstatus(String nstatus) {
		this.eq_nstatus = nstatus;
	}
}