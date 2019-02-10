package org.ljdp.log.model;

import java.util.Date;

public interface RequestErrorLog extends java.io.Serializable{

	public String getErrorId();
	public void setErrorId(String errorId);
	
	public String getRequestId();
	public void setRequestId(String requestId);
	
	public String getError();
	public void setError(String error);
	
	public Date getErrorDate();
	public void setErrorDate(Date errorDate);
}
