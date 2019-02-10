package org.ljdp.common.timer.thread;

public class TimingActivation implements Runnable {
	private InstanceScaner iscaner;

	public TimingActivation(InstanceScaner iscaner) {
		super();
		this.iscaner = iscaner;
	}

	public void run() {
		if(iscaner != null) {
			iscaner.setBeginOwnActive(false);
			synchronized (iscaner) {
				iscaner.notify();
			}
		}
	}

}
