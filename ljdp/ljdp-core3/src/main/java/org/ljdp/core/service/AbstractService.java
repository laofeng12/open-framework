package org.ljdp.core.service;

import org.ljdp.component.user.DBAccessUser;

public abstract class AbstractService implements Authorizable {
	private DBAccessUser user;
	
	public DBAccessUser getUser() {
		return user;
	}

	public void setUser(DBAccessUser user) {
		this.user = user;
	}

	public String getUserAccessDB() {
		DBAccessUser user = getUser();
		if(user != null) {
			return user.getAccessDB();
		}
		return DBAccessUser.getInnerUser().getAccessDB();
	}

}
