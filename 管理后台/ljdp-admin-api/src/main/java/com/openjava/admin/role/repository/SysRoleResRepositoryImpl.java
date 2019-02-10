package com.openjava.admin.role.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysRoleResRepositoryImpl implements SysRoleResRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
