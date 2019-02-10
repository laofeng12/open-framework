package org.ljdp.component.exception;

public class MServiceCallException extends RuntimeException {

	private static final long serialVersionUID = 6167896505041933073L;
	
	public Integer code;
	
	public MServiceCallException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
