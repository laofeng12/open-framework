package org.ljdp.component.exception;

/**
 * 业务失败异常，非系统问题
 * @author hzy
 *
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4731959504796406911L;

	public Integer code;
	
	public BusinessException(Integer code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String msg) {
		super(msg);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
