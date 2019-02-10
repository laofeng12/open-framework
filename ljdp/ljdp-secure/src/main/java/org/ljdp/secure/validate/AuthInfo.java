package org.ljdp.secure.validate;

import java.util.Date;

public interface AuthInfo {

	public String getTokenid();
	public void setTokenid(String tokenid);
	

	public Long getPassId();
	public void setPassId(Long passId);

	public String getUserAgent();
	public void setUserAgent(String userAgent);

	public String getDevType();
	public void setDevType(String devType);
	
	public Date getLoginTime();
	public void setLoginTime(Date loginTime);
	
	public Date getLogoutTime();
	public void setLogoutTime(Date logoutTime);
	
	public String getState();
	public void setState(String state);
}
