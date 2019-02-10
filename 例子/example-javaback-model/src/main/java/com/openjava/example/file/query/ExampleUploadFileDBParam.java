package com.openjava.example.file.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author hzy
 *
 */
public class ExampleUploadFileDBParam extends RoDBQueryParam {
	private Long eq_fid;//文件id --主键查询
	
	private String like_fname;//文件名称 like ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date le_createTime;//创建时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date ge_createTime;//创建时间 >= ?
	
	public Long getEq_fid() {
		return eq_fid;
	}
	public void setEq_fid(Long fid) {
		this.eq_fid = fid;
	}
	
	public String getLike_fname() {
		return like_fname;
	}
	public void setLike_fname(String fname) {
		this.like_fname = fname;
	}
	public Date getLe_createTime() {
		return le_createTime;
	}
	public void setLe_createTime(Date createTime) {
		this.le_createTime = createTime;
	}
	public Date getGe_createTime() {
		return ge_createTime;
	}
	public void setGe_createTime(Date createTime) {
		this.ge_createTime = createTime;
	}
}