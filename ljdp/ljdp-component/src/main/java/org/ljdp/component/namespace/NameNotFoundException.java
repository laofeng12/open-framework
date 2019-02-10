package org.ljdp.component.namespace;

public class NameNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5708905814764724020L;

	public NameNotFoundException(String msg) {
		super(msg);
	}
	
	public NameNotFoundException() {
		super("the name is not know or not found");
	}
}
