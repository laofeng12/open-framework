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
@ApiModel("角色")
@Entity
@Table(name = "SYS_ROLE")
public class SysRole extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("角色id")
	private Long roleid;//角色id
	@ApiModelProperty("系统编码")
	private Long systemid;//系统编码
	@ApiModelProperty("别名")
	private String alias;//别名
	@ApiModelProperty("角色名称")
	private String rolename;//角色名称
	@ApiModelProperty("备注")
	private String memo;//备注
	@ApiModelProperty("是否允许删除")
	private Short allowdel;//是否允许删除
	private String allowdelName;
	@ApiModelProperty("是否允许编辑")
	private Short allowedit;//是否允许编辑
	private String alloweditName;
	@ApiModelProperty("是否启用")
	private Short enabled;//是否启用
	private String enabledName;
	
	@Id
	@Column(name = "ROLEID")
	public Long getRoleid() {
		return roleid;
	}
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	

	@Column(name = "SYSTEMID")
	public Long getSystemid() {
		return systemid;
	}
	public void setSystemid(Long systemid) {
		this.systemid = systemid;
	}
	

	@Column(name = "ALIAS")
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	

	@Column(name = "ROLENAME")
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	

	@Column(name = "MEMO")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	

	@Column(name = "ALLOWDEL")
	public Short getAllowdel() {
		return allowdel;
	}
	public void setAllowdel(Short allowdel) {
		this.allowdel = allowdel;
	}
	
	@Transient
	public String getAllowdelName() {
		return allowdelName;
	}
	public void setAllowdelName(String allowdelName) {
		this.allowdelName = allowdelName;
	}

	@Column(name = "ALLOWEDIT")
	public Short getAllowedit() {
		return allowedit;
	}
	public void setAllowedit(Short allowedit) {
		this.allowedit = allowedit;
	}
	
	@Transient
	public String getAlloweditName() {
		return alloweditName;
	}
	public void setAlloweditName(String alloweditName) {
		this.alloweditName = alloweditName;
	}

	@Column(name = "ENABLED")
	public Short getEnabled() {
		return enabled;
	}
	public void setEnabled(Short enabled) {
		this.enabled = enabled;
	}
	
	@Transient
	public String getEnabledName() {
		return enabledName;
	}
	public void setEnabledName(String enabledName) {
		this.enabledName = enabledName;
	}
}