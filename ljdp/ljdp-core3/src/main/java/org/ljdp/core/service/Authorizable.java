package org.ljdp.core.service;

import org.ljdp.component.user.DBAccessUser;

public interface Authorizable {
	DBAccessUser getUser();
	
	void setUser(DBAccessUser user);
}
