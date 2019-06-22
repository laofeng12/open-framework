package org.ljdp.component.result;

import java.io.Serializable;

public class SuccessMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7882454953469908760L;
	private String requestId;
	private String message;
	
	public SuccessMessage() {
		this.message = "成功";
	}
	public SuccessMessage(String message) {
		super();
		this.message = message;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
