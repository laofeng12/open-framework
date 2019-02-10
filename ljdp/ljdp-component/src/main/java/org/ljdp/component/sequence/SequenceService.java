package org.ljdp.component.sequence;


public interface SequenceService {
	public String getSequence(String t) throws SequenceException;
	
	public long getSequence() throws SequenceException;
}
