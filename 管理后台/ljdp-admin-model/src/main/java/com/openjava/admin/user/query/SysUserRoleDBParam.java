package com.openjava.admin.user.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysUserRoleDBParam extends RoDBQueryParam {
	private Long eq_userroleid;//用户角色关系id --主键查询
	
	private Long eq_roleid;//角色ID = ?
	private Long eq_userid;//用户id = ?
	
	public Long getEq_userroleid() {
		return eq_userroleid;
	}
	public void setEq_userroleid(Long userroleid) {
		this.eq_userroleid = userroleid;
	}
	
	public Long getEq_roleid() {
		return eq_roleid;
	}
	public void setEq_roleid(Long roleid) {
		this.eq_roleid = roleid;
	}
	public Long getEq_userid() {
		return eq_userid;
	}
	public void setEq_userid(Long userid) {
		this.eq_userid = userid;
	}
}