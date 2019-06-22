package org.ljdp.common;

import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.TimeSequence;

public class SequenceTest {

	public static void main(String[] args) {
		System.out.println(ConcurrentSequence.getInstance().getSequence());
		System.out.println(TimeSequence.getInstance().getSequence());

	}

}
