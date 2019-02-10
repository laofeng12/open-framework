package org.ljdp.component.session;

public class SessionOverdueException extends Exception {
	private static final long serialVersionUID = -1330154504065817092L;

	public SessionOverdueException(String msg) {
		super(msg);
	}
	
	public SessionOverdueException(Exception e) {
		super(e);
	}
}
