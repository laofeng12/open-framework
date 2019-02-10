package com.openjava.admin.user.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysUserRoleRepositoryImpl implements SysUserRoleRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
