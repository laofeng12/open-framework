package com.openjava.admin.res.domain;

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
@ApiModel("菜单资源")
@Entity
@Table(name = "SYS_RES")
public class SysRes extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("资源ID")
	private Long resid;//资源ID
	@ApiModelProperty("资源名称")
	private String resname;//资源名称
	@ApiModelProperty("资源别名")
	private String alias;//资源别名
	private Long sn;//SN
	@ApiModelProperty("图标")
	private String icon;//图标
	@ApiModelProperty("父资源id")
	private Long parentid;//父资源id
	@ApiModelProperty("默认URL")
	private String defaulturl;//默认URL
	@ApiModelProperty("可否展开")
	private Short isfolder;//可否展开
	private String isfolderName;
	@ApiModelProperty("是否展示菜单")
	private Short isdisplayinmenu;//是否展示菜单
	private String isdisplayinmenuName;
	@ApiModelProperty("是否打开")
	private Short isopen;//是否打开
	@ApiModelProperty("是否打开翻译")
	private String isopenName;
	@ApiModelProperty("系统编码")
	private Long systemid;//系统编码
	@ApiModelProperty("完整路径")
	private String path;//完整路径
	@ApiModelProperty("排序")
	private Integer sort;//排序
	
	@Id
	@Column(name = "RESID")
	public Long getResid() {
		return resid;
	}
	public void setResid(Long resid) {
		this.resid = resid;
	}
	

	@Column(name = "RESNAME")
	public String getResname() {
		return resname;
	}
	public void setResname(String resname) {
		this.resname = resname;
	}
	

	@Column(name = "ALIAS")
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	

	@Column(name = "SN")
	public Long getSn() {
		return sn;
	}
	public void setSn(Long sn) {
		this.sn = sn;
	}
	

	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	

	@Column(name = "PARENTID")
	public Long getParentid() {
		return parentid;
	}
	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}
	

	@Column(name = "DEFAULTURL")
	public String getDefaulturl() {
		return defaulturl;
	}
	public void setDefaulturl(String defaulturl) {
		this.defaulturl = defaulturl;
	}
	

	@Column(name = "ISFOLDER")
	public Short getIsfolder() {
		return isfolder;
	}
	public void setIsfolder(Short isfolder) {
		this.isfolder = isfolder;
	}
	
	@Transient
	public String getIsfolderName() {
		return isfolderName;
	}
	public void setIsfolderName(String isfolderName) {
		this.isfolderName = isfolderName;
	}

	@Column(name = "ISDISPLAYINMENU")
	public Short getIsdisplayinmenu() {
		return isdisplayinmenu;
	}
	public void setIsdisplayinmenu(Short isdisplayinmenu) {
		this.isdisplayinmenu = isdisplayinmenu;
	}
	
	@Transient
	public String getIsdisplayinmenuName() {
		return isdisplayinmenuName;
	}
	public void setIsdisplayinmenuName(String isdisplayinmenuName) {
		this.isdisplayinmenuName = isdisplayinmenuName;
	}

	@Column(name = "ISOPEN")
	public Short getIsopen() {
		return isopen;
	}
	public void setIsopen(Short isopen) {
		this.isopen = isopen;
	}
	
	@Transient
	public String getIsopenName() {
		return isopenName;
	}
	public void setIsopenName(String isopenName) {
		this.isopenName = isopenName;
	}

	@Column(name = "SYSTEMID")
	public Long getSystemid() {
		return systemid;
	}
	public void setSystemid(Long systemid) {
		this.systemid = systemid;
	}
	

	@Column(name = "PATH")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(name = "SORT")
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}