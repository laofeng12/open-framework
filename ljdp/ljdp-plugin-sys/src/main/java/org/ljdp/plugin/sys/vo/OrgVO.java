package org.ljdp.plugin.sys.vo;

import java.io.Serializable;

public class OrgVO implements Serializable{
	private static final long serialVersionUID = -55123560626553199L;
	private Long orgId;//组织ID
	private String orgName;//组织名称
	private Long parentId;//上级组织
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
