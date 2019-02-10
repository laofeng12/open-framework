package com.openjava.admin.user.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysUserDBParam extends RoDBQueryParam {
	private Long eq_userid;//用户id --主键查询
	
	private String like_fullname;//名称 like ?
	private String eq_account;//登录账号 = ?
	private String eq_accounttype;//帐号类型(SYS.AccountType) = ?
	private Short eq_isexpired;//是否过期 = ?
	private Short eq_islock;//是否锁定 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date le_createtime;//创建时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ge_createtime;//创建时间 >= ?
	private Short eq_status;//状态 = ?
	private String eq_mobile;//手机号码 = ?
	private Short eq_fromtype;//来源类型 = ?
	
	public Long getEq_userid() {
		return eq_userid;
	}
	public void setEq_userid(Long userid) {
		this.eq_userid = userid;
	}
	
	public String getLike_fullname() {
		return like_fullname;
	}
	public void setLike_fullname(String fullname) {
		this.like_fullname = fullname;
	}
	public String getEq_account() {
		return eq_account;
	}
	public void setEq_account(String account) {
		this.eq_account = account;
	}
	public String getEq_accounttype() {
		return eq_accounttype;
	}
	public void setEq_accounttype(String accounttype) {
		this.eq_accounttype = accounttype;
	}
	public Short getEq_isexpired() {
		return eq_isexpired;
	}
	public void setEq_isexpired(Short isexpired) {
		this.eq_isexpired = isexpired;
	}
	public Short getEq_islock() {
		return eq_islock;
	}
	public void setEq_islock(Short islock) {
		this.eq_islock = islock;
	}
	public Date getLe_createtime() {
		return le_createtime;
	}
	public void setLe_createtime(Date createtime) {
		this.le_createtime = createtime;
	}
	public Date getGe_createtime() {
		return ge_createtime;
	}
	public void setGe_createtime(Date createtime) {
		this.ge_createtime = createtime;
	}
	public Short getEq_status() {
		return eq_status;
	}
	public void setEq_status(Short status) {
		this.eq_status = status;
	}
	public String getEq_mobile() {
		return eq_mobile;
	}
	public void setEq_mobile(String mobile) {
		this.eq_mobile = mobile;
	}
	public Short getEq_fromtype() {
		return eq_fromtype;
	}
	public void setEq_fromtype(Short fromtype) {
		this.eq_fromtype = fromtype;
	}
}