package com.openjava.admin.sys.domain;

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
@ApiModel("子系统")
@Entity
@Table(name = "SYS_SUBSYSTEM")
public class SysSubsystem extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("系统id")
	private Long systemid;//系统id
	@ApiModelProperty("系统名称")
	private String sysname;//系统名称
	@ApiModelProperty("别名")
	private String alias;//别名
	@ApiModelProperty("系统LOGO")
	private String logo;//系统LOGO
	@ApiModelProperty("默认地址")
	private String defaulturl;//默认地址
	@ApiModelProperty("备注")
	private String memo;//备注
	@ApiModelProperty("创建时间")
	private Date createtime;//创建时间
	@ApiModelProperty("创建人员")
	private String creator;//创建人员
	@ApiModelProperty("是否允许删除")
	private Short allowdel;//是否允许删除
	private String allowdelName;
	@ApiModelProperty("是否需要组织")
	private Short needorg;//是否需要组织
	private String needorgName;
	@ApiModelProperty("是否激活")
	private Short isactive;//是否激活
	private String isactiveName;
	@ApiModelProperty("是否本地")
	private Short islocal;//是否本地
	private String islocalName;
	@ApiModelProperty("主页")
	private String homepage;//主页
	
	@Id
	@Column(name = "SYSTEMID")
	public Long getSystemid() {
		return systemid;
	}
	public void setSystemid(Long systemid) {
		this.systemid = systemid;
	}
	

	@Column(name = "SYSNAME")
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	

	@Column(name = "ALIAS")
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	

	@Column(name = "LOGO")
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	

	@Column(name = "DEFAULTURL")
	public String getDefaulturl() {
		return defaulturl;
	}
	public void setDefaulturl(String defaulturl) {
		this.defaulturl = defaulturl;
	}
	

	@Column(name = "MEMO")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	

	@Column(name = "CREATOR")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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

	@Column(name = "NEEDORG")
	public Short getNeedorg() {
		return needorg;
	}
	public void setNeedorg(Short needorg) {
		this.needorg = needorg;
	}
	
	@Transient
	public String getNeedorgName() {
		return needorgName;
	}
	public void setNeedorgName(String needorgName) {
		this.needorgName = needorgName;
	}

	@Column(name = "ISACTIVE")
	public Short getIsactive() {
		return isactive;
	}
	public void setIsactive(Short isactive) {
		this.isactive = isactive;
	}
	
	@Transient
	public String getIsactiveName() {
		return isactiveName;
	}
	public void setIsactiveName(String isactiveName) {
		this.isactiveName = isactiveName;
	}

	@Column(name = "ISLOCAL")
	public Short getIslocal() {
		return islocal;
	}
	public void setIslocal(Short islocal) {
		this.islocal = islocal;
	}
	
	@Transient
	public String getIslocalName() {
		return islocalName;
	}
	public void setIslocalName(String islocalName) {
		this.islocalName = islocalName;
	}

	@Column(name = "HOMEPAGE")
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
}