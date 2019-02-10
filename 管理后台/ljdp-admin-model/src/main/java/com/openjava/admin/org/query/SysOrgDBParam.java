package com.openjava.admin.org.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class SysOrgDBParam extends RoDBQueryParam {
	private Long eq_orgid;//组织ID --主键查询
	
	private String like_orgname;//组织名称 like ?
	private Long eq_orgsupid;//上级组织 = ?
	private Long eq_orgtype;//组织类型 = ?
	
	public Long getEq_orgid() {
		return eq_orgid;
	}
	public void setEq_orgid(Long orgid) {
		this.eq_orgid = orgid;
	}
	
	public String getLike_orgname() {
		return like_orgname;
	}
	public void setLike_orgname(String orgname) {
		this.like_orgname = orgname;
	}
	public Long getEq_orgsupid() {
		return eq_orgsupid;
	}
	public void setEq_orgsupid(Long orgsupid) {
		this.eq_orgsupid = orgsupid;
	}
	public Long getEq_orgtype() {
		return eq_orgtype;
	}
	public void setEq_orgtype(Long orgtype) {
		this.eq_orgtype = orgtype;
	}
}