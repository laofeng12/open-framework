package com.openjava.admin.org.domain;

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
@ApiModel("组织机构")
@Entity
@Table(name = "SYS_ORG")
public class SysOrg extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("组织ID")
	private Long orgid;//组织ID
	private Long demid;//DEMID
	@ApiModelProperty("组织名称")
	private String orgname;//组织名称
	@ApiModelProperty("组织描述")
	private String orgdesc;//组织描述
	@ApiModelProperty("上级组织")
	private Long orgsupid;//上级组织
	@ApiModelProperty("路径")
	private String path;//路径
	private Long depth;//DEPTH
	@ApiModelProperty("组织类型")
	private Long orgtype;//组织类型
	@ApiModelProperty("")
	private String orgtypeName;
	@ApiModelProperty("创建人id")
	private Long creatorid;//创建人id
	@ApiModelProperty("创建时间")
	private Date createtime;//创建时间
	@ApiModelProperty("更新人id")
	private Long updateid;//更新人id
	@ApiModelProperty("更新时间")
	private Date updatetime;//更新时间
	private Long sn;//SN
	@ApiModelProperty("来源")
	private Short fromtype;//来源
	@ApiModelProperty("路径全名")
	private String orgpathname;//路径全名
	
	@Id
	@Column(name = "ORGID")
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	

	@Column(name = "DEMID")
	public Long getDemid() {
		return demid;
	}
	public void setDemid(Long demid) {
		this.demid = demid;
	}
	

	@Column(name = "ORGNAME")
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	

	@Column(name = "ORGDESC")
	public String getOrgdesc() {
		return orgdesc;
	}
	public void setOrgdesc(String orgdesc) {
		this.orgdesc = orgdesc;
	}
	

	@Column(name = "ORGSUPID")
	public Long getOrgsupid() {
		return orgsupid;
	}
	public void setOrgsupid(Long orgsupid) {
		this.orgsupid = orgsupid;
	}
	

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	

	@Column(name = "DEPTH")
	public Long getDepth() {
		return depth;
	}
	public void setDepth(Long depth) {
		this.depth = depth;
	}
	

	@Column(name = "ORGTYPE")
	public Long getOrgtype() {
		return orgtype;
	}
	public void setOrgtype(Long orgtype) {
		this.orgtype = orgtype;
	}
	
	@Transient
	public String getOrgtypeName() {
		return orgtypeName;
	}
	public void setOrgtypeName(String orgtypeName) {
		this.orgtypeName = orgtypeName;
	}

	@Column(name = "CREATORID")
	public Long getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(Long creatorid) {
		this.creatorid = creatorid;
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
	

	@Column(name = "UPDATEID")
	public Long getUpdateid() {
		return updateid;
	}
	public void setUpdateid(Long updateid) {
		this.updateid = updateid;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME")
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	

	@Column(name = "SN")
	public Long getSn() {
		return sn;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	}
	

	@Column(name = "FROMTYPE")
	public Short getFromtype() {
		return fromtype;
	}
	public void setFromtype(Short fromtype) {
		this.fromtype = fromtype;
	}
	

	@Column(name = "ORGPATHNAME")
	public String getOrgpathname() {
		return orgpathname;
	}
	public void setOrgpathname(String orgpathname) {
		this.orgpathname = orgpathname;
	}
	
}