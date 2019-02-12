package com.openjava.admin.logs.query;

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author heziyou
 *
 */
public class LogApiErrorDBParam extends RoDBQueryParam {
	private String eq_errorId;//错误ID --主键查询
	
	private String eq_requestId;//接口请求ID = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_errorDate;//错误时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_errorDate;//错误时间 >= ?
	
	public String getEq_errorId() {
		return eq_errorId;
	}
	public void setEq_errorId(String errorId) {
		this.eq_errorId = errorId;
	}
	
	public String getEq_requestId() {
		return eq_requestId;
	}
	public void setEq_requestId(String requestId) {
		this.eq_requestId = requestId;
	}
	public Date getLe_errorDate() {
		return le_errorDate;
	}
	public void setLe_errorDate(Date errorDate) {
		this.le_errorDate = errorDate;
	}
	public Date getGe_errorDate() {
		return ge_errorDate;
	}
	public void setGe_errorDate(Date errorDate) {
		this.ge_errorDate = errorDate;
	}
}