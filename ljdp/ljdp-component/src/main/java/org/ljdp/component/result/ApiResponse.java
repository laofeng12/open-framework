package org.ljdp.component.result;

import java.io.Serializable;

public interface ApiResponse extends Serializable{
	
	public String getRequestId();
	public void setRequestId(String requestId);
	public Integer getCode();
	public void setCode(Integer code);
	
	public String getMessage();
	public void setMessage(String message);
	
	public String getTub();
	public void setTub(String tub);
	
//	public Object getData();
//	public void setData(Object data);
}
