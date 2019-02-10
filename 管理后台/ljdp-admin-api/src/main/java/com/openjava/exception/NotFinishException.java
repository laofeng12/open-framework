package com.openjava.exception;

/**
 * 没用执行完成
 * @author hzy0769
 *
 */
public class NotFinishException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6390946826355464265L;

	public NotFinishException(String msg) {
		super(msg);
	}
}
