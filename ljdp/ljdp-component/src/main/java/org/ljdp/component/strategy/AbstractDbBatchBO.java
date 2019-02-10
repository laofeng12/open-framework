package org.ljdp.component.strategy;

import org.ljdp.component.user.DBAccessUser;

public abstract class AbstractDbBatchBO implements DbBatchBusinessObject {
	private String operType;
	private DBAccessUser user;
	private String batchID;
	
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public DBAccessUser getUser() {
		return user;
	}

	public void setUser(DBAccessUser user) {
		this.user = user;
	}

	public String getBatchID() {
		return batchID;
	}

	public void setBatchID(String batchID) {
		this.batchID = batchID;
	}

}
