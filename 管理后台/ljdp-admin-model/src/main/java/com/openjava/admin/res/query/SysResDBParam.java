package com.openjava.admin.res.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysResDBParam extends RoDBQueryParam {
	private Long eq_resid;//资源ID --主键查询
	
	private String like_resname;//资源名称 like ?
	private Short eq_isfolder;//可否展开 = ?
	private Short eq_isdisplayinmenu;//是否默认菜单 = ?
	private Short eq_isopen;//是否打开 = ?
	private Long eq_parentid;//父资源id = ?
	private String like_defaulturl;//默认URL like ?
	
	public Long getEq_resid() {
		return eq_resid;
	}
	public void setEq_resid(Long resid) {
		this.eq_resid = resid;
	}
	
	public String getLike_resname() {
		return like_resname;
	}
	public void setLike_resname(String resname) {
		this.like_resname = resname;
	}
	public Short getEq_isfolder() {
		return eq_isfolder;
	}
	public void setEq_isfolder(Short isfolder) {
		this.eq_isfolder = isfolder;
	}
	public Short getEq_isdisplayinmenu() {
		return eq_isdisplayinmenu;
	}
	public void setEq_isdisplayinmenu(Short isdisplayinmenu) {
		this.eq_isdisplayinmenu = isdisplayinmenu;
	}
	public Short getEq_isopen() {
		return eq_isopen;
	}
	public void setEq_isopen(Short isopen) {
		this.eq_isopen = isopen;
	}
	public Long getEq_parentid() {
		return eq_parentid;
	}
	public void setEq_parentid(Long parentid) {
		this.eq_parentid = parentid;
	}
	public String getLike_defaulturl() {
		return like_defaulturl;
	}
	public void setLike_defaulturl(String defaulturl) {
		this.like_defaulturl = defaulturl;
	}
}