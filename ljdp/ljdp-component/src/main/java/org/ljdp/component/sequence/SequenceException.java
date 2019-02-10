package org.ljdp.component.sequence;

public class SequenceException extends RuntimeException {
    private static final long serialVersionUID = 4039701938246656636L;

    public SequenceException() {
    	super("sequence build error");
    }

    public SequenceException(String message) {
        super(message);
    }
}
