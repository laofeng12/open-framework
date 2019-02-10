package com.openjava.admin.role.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysRoleDBParam extends RoDBQueryParam {
	private Long eq_roleid;//角色id --主键查询
	
	private String like_rolename;//角色名称 like ?
	private String like_alias;//别名
	private Short eq_allowdel;//是否允许删除 = ?
	private Short eq_allowedit;//是否允许编辑 = ?
	private Short eq_enabled;//是否启用 = ?
	
	public Long getEq_roleid() {
		return eq_roleid;
	}
	public void setEq_roleid(Long roleid) {
		this.eq_roleid = roleid;
	}
	
	public String getLike_rolename() {
		return like_rolename;
	}
	public void setLike_rolename(String rolename) {
		this.like_rolename = rolename;
	}
	public Short getEq_allowdel() {
		return eq_allowdel;
	}
	public void setEq_allowdel(Short allowdel) {
		this.eq_allowdel = allowdel;
	}
	public Short getEq_allowedit() {
		return eq_allowedit;
	}
	public void setEq_allowedit(Short allowedit) {
		this.eq_allowedit = allowedit;
	}
	public Short getEq_enabled() {
		return eq_enabled;
	}
	public void setEq_enabled(Short enabled) {
		this.eq_enabled = enabled;
	}
	public String getLike_alias() {
		return like_alias;
	}
	public void setLike_alias(String like_alias) {
		this.like_alias = like_alias;
	}
}