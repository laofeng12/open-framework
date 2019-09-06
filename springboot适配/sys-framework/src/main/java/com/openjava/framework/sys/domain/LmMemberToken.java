package com.openjava.framework.sys.domain;

import java.util.Date;

import javax.persistence.*;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.secure.validate.AuthInfo;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实体
 * @author hzy
 *
 */
//@Entity
//@Table(name = "MS_MEMBER_TOKEN")
public class LmMemberToken extends BasicApiResponse implements ApiResponse,AuthInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1384128631047609827L;
	private String tokenid;//令牌
	private Long passId;//通行证
	private String userAgent;//客户端信息
	private String devType;//设备类型
	private Date loginTime;//登录时间
	private Date logoutTime;//退出时间
	private String state;//状态（1：登录中，2：退出，3：其他设备登录导致失效，4：过期）
	
	@Id
	@Column(name = "TOKENID")
	public String getTokenid() {
		return tokenid;
	}
	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
	

	@Column(name = "PASS_ID")
	public Long getPassId() {
		return passId;
	}
	public void setPassId(Long passId) {
		this.passId = passId;
	}
	

	@Column(name = "USER_AGENT")
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	

	@Column(name = "DEV_TYPE")
	public String getDevType() {
		return devType;
	}
	public void setDevType(String devType) {
		this.devType = devType;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_TIME")
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGOUT_TIME")
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	

	@Column(name = "STATE")
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}