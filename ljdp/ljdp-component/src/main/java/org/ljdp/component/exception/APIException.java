package org.ljdp.component.exception;

public class APIException extends Exception {
	private static final long serialVersionUID = -8467257091128458758L;
	public Integer code;

	public APIException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public APIException(String msg) {
		super(msg);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
