package org.ljdp.component.result;

public class GeneralResult implements Result{
	private static final long serialVersionUID = -940139127925466172L;

	private String msg;

    private boolean success = false;
    
    private int totalCount;
    
    private int current;
    
    private Object data;

    public GeneralResult() {
    }
    
    public GeneralResult(boolean succ) {
    	success = succ;
    }

    public void setSuccess(boolean ok) {
        this.success = ok;
    }

    public boolean isSuccess() {
        return success;
    }

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
