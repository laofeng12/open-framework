package org.ljdp.component.result;

public interface DataBaseResult extends DelayResult {
	public boolean isRollback();
	public void rollBack();
}
