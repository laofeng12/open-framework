package com.openjava.admin.user.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysUserOrgDBParam extends RoDBQueryParam {
	private Long eq_userorgid;//用户机构关系id --主键查询
	
	private Long eq_orgid;//组织机构编码 = ?
	private Long eq_userid;//用户编码 = ?
	
	public Long getEq_userorgid() {
		return eq_userorgid;
	}
	public void setEq_userorgid(Long userorgid) {
		this.eq_userorgid = userorgid;
	}
	
	public Long getEq_orgid() {
		return eq_orgid;
	}
	public void setEq_orgid(Long orgid) {
		this.eq_orgid = orgid;
	}
	public Long getEq_userid() {
		return eq_userid;
	}
	public void setEq_userid(Long userid) {
		this.eq_userid = userid;
	}
}