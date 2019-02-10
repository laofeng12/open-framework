package org.ljdp.component.session;

public class RequestErrorException extends Exception {
	private static final long serialVersionUID = -6636231294097889513L;

	public RequestErrorException(String msg) {
		super(msg);
	}
	
	public RequestErrorException(Exception e) {
		super(e);
	}
}
