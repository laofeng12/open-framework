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
@ApiModel("用户组织")
@Entity
@Table(name = "SYS_USER_ORG")
public class SysUserOrg extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("用户机构关系id")
	private Long userorgid;//用户机构关系id
	@ApiModelProperty("组织机构编码")
	private Long orgid;//组织机构编码
	@ApiModelProperty("用户编码")
	private Long userid;//用户编码
	@ApiModelProperty("是否主")
	private Short isprimary;//是否主
	private Short ischarge;//ISCHARGE
	private Short isgrademanage;//ISGRADEMANAGE
	
	@ApiModelProperty("组织名称")
	private String orgname;
	@ApiModelProperty("路径全名")
	private String orgpathname;//路径全名
	
	@Id
	@Column(name = "USERORGID")
	public Long getUserorgid() {
		return userorgid;
	}
	public void setUserorgid(Long userorgid) {
		this.userorgid = userorgid;
	}
	

	@Column(name = "ORGID")
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	

	@Column(name = "USERID")
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	

	@Column(name = "ISPRIMARY")
	public Short getIsprimary() {
		return isprimary;
	}
	public void setIsprimary(Short isprimary) {
		this.isprimary = isprimary;
	}
	

	@Column(name = "ISCHARGE")
	public Short getIscharge() {
		return ischarge;
	}
	public void setIscharge(Short ischarge) {
		this.ischarge = ischarge;
	}
	

	@Column(name = "ISGRADEMANAGE")
	public Short getIsgrademanage() {
		return isgrademanage;
	}
	public void setIsgrademanage(Short isgrademanage) {
		this.isgrademanage = isgrademanage;
	}
	
	@Transient
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	@Transient
	public String getOrgpathname() {
		return orgpathname;
	}
	public void setOrgpathname(String orgpathname) {
		this.orgpathname = orgpathname;
	}
	
}