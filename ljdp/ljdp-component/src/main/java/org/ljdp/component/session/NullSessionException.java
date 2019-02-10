package org.ljdp.component.session;

public class NullSessionException extends Exception {
	private static final long serialVersionUID = -2094105364917249201L;

	public NullSessionException(String msg) {
		super(msg);
	}
	
	public NullSessionException(Exception e) {
		super(e);
	}
}
