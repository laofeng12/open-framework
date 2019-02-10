package org.ljdp.component.user;

public interface UserRoleChecker {
	
	/**
	 * 是否超级管理员
	 * @return
	 */
	public boolean isAdministrator(DBAccessUser user);
}
