package org.ljdp.component.result;


public interface BatchResult extends Result{

	public int getTotalCount();

	public void setTotalCount(int totalCount);

	public int getCurrent();

	public void setCurrent(int current);
	
	public String getInputFileName();
	
	public void setInputFileName(String name);
	
	public String getOutputFileName();
	
	public void setOutputFileName(String saveFileName);
	
	public String getErrorFileName();
	
	public void setErrorFileName(String errorFileName);
	
	public String getSuccessFileName();
	
	public void setSuccessFileName(String successFileName);
	
	public int getOk();
	
	public void setOk(int ok);
	
	public int getFail();
	
	public void setFail(int fail);

}
