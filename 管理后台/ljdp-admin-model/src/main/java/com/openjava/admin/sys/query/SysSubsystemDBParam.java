package com.openjava.admin.sys.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysSubsystemDBParam extends RoDBQueryParam {
	private Long eq_systemid;//系统id --主键查询
	
	private String like_sysname;//系统名称 like ?
	private Short eq_allowdel;//是否允许删除 = ?
	private Short eq_needorg;//是否需要组织 = ?
	private Short eq_isactive;//是否激活 = ?
	private Short eq_islocal;//是否本地 = ?
	
	public Long getEq_systemid() {
		return eq_systemid;
	}
	public void setEq_systemid(Long systemid) {
		this.eq_systemid = systemid;
	}
	
	public String getLike_sysname() {
		return like_sysname;
	}
	public void setLike_sysname(String sysname) {
		this.like_sysname = sysname;
	}
	public Short getEq_allowdel() {
		return eq_allowdel;
	}
	public void setEq_allowdel(Short allowdel) {
		this.eq_allowdel = allowdel;
	}
	public Short getEq_needorg() {
		return eq_needorg;
	}
	public void setEq_needorg(Short needorg) {
		this.eq_needorg = needorg;
	}
	public Short getEq_isactive() {
		return eq_isactive;
	}
	public void setEq_isactive(Short isactive) {
		this.eq_isactive = isactive;
	}
	public Short getEq_islocal() {
		return eq_islocal;
	}
	public void setEq_islocal(Short islocal) {
		this.eq_islocal = islocal;
	}
}