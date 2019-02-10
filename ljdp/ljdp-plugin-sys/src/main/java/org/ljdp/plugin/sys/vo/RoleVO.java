package org.ljdp.plugin.sys.vo;

import java.io.Serializable;

public class RoleVO implements Serializable{
	private static final long serialVersionUID = -3265638120065969235L;
	private String roleId;
	private String roleName;
	private String roleType;
	private String roleAlias;
	
	public RoleVO() {
		
	}
	
	public RoleVO(String roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleAlias() {
		return roleAlias;
	}

	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}
	
}
