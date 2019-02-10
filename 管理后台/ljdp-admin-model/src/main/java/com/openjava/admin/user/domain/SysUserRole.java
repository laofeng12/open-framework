package com.openjava.admin.user.domain;

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
@ApiModel("用户角色")
@Entity
@Table(name = "SYS_USER_ROLE")
public class SysUserRole extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("用户角色关系id")
	private Long userroleid;//用户角色关系id
	@ApiModelProperty("角色ID")
	private Long roleid;//角色ID
	@ApiModelProperty("用户id")
	private Long userid;//用户id
	@ApiModelProperty("角色名称")
	private String rolename;//角色名称
	
	@Id
	@Column(name = "USERROLEID")
	public Long getUserroleid() {
		return userroleid;
	}
	public void setUserroleid(Long userroleid) {
		this.userroleid = userroleid;
	}
	

	@Column(name = "ROLEID")
	public Long getRoleid() {
		return roleid;
	}
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	

	@Column(name = "USERID")
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	@Transient
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
}