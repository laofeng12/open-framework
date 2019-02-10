package org.ljdp.component.result;

import java.io.Serializable;

public interface Result extends Serializable{

	public void setSuccess(boolean ok);

	public boolean isSuccess();

	public String getMsg();

	public void setMsg(String msg);
	
	public Object getData();
	public void setData(Object data);
}
