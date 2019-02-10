package org.ljdp.component.result;

public class GeneralDbResult implements DataBaseResult {
	private static final long serialVersionUID = -4796060372695030652L;
	private boolean success;
    private String msg;
    private boolean rollback = false;
    private long delay = 0;
    
    private Object data;

	public boolean isRollback() {
		return rollback;
	}

	public void rollBack() {
		this.rollback = true;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
