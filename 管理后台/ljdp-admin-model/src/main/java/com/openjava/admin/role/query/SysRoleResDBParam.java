package com.openjava.admin.role.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysRoleResDBParam extends RoDBQueryParam {
	private Long eq_roleresid;//角色资源权限id --主键查询
	
	private Long eq_resid;//资源id = ?
	private Long eq_roleid;//角色id = ?
	private Long eq_systemid;//系统编码 = ?
	
	public Long getEq_roleresid() {
		return eq_roleresid;
	}
	public void setEq_roleresid(Long roleresid) {
		this.eq_roleresid = roleresid;
	}
	
	public Long getEq_resid() {
		return eq_resid;
	}
	public void setEq_resid(Long resid) {
		this.eq_resid = resid;
	}
	public Long getEq_roleid() {
		return eq_roleid;
	}
	public void setEq_roleid(Long roleid) {
		this.eq_roleid = roleid;
	}
	public Long getEq_systemid() {
		return eq_systemid;
	}
	public void setEq_systemid(Long systemid) {
		this.eq_systemid = systemid;
	}
}