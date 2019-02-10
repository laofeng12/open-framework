package com.openjava.admin.role.domain;

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
 * @author hzy
 *
 */
@ApiModel("角色资源")
@Entity
@Table(name = "SYS_ROLE_RES")
public class SysRoleRes extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("角色资源权限id")
	private Long roleresid;//角色资源权限id
	@ApiModelProperty("角色id")
	private Long roleid;//角色id
	@ApiModelProperty("资源id")
	private Long resid;//资源id
	@ApiModelProperty("系统编码")
	private Long systemid;//系统编码
	
	@Id
	@Column(name = "ROLERESID")
	public Long getRoleresid() {
		return roleresid;
	}
	public void setRoleresid(Long roleresid) {
		this.roleresid = roleresid;
	}
	

	@Column(name = "ROLEID")
	public Long getRoleid() {
		return roleid;
	}
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	

	@Column(name = "RESID")
	public Long getResid() {
		return resid;
	}
	public void setResid(Long resid) {
		this.resid = resid;
	}
	

	@Column(name = "SYSTEMID")
	public Long getSystemid() {
		return systemid;
	}
	public void setSystemid(Long systemid) {
		this.systemid = systemid;
	}
	
}