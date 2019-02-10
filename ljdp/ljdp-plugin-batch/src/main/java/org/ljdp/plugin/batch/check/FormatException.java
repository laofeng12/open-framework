package org.ljdp.plugin.batch.check;

public class FormatException extends Exception {
	private static final long serialVersionUID = 7532869424319011078L;

	public FormatException() {
		super("格式不正确");
	}
	
	public FormatException(String msg) {
		super(msg);
	}
	
	public FormatException(Exception e) {
		super(e);
	}
}
