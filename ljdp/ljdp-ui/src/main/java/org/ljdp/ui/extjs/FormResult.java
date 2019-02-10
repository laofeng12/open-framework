package org.ljdp.ui.extjs;

import org.ljdp.component.result.Result;

public class FormResult implements Result {
	private static final long serialVersionUID = 8676467437905912106L;
	private boolean success = false;
	private String msg;
	private Object data;

	public String getMsg() {
		return msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setSuccess(boolean ok) {
		this.success = ok;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
