package com.openjava.admin.role.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysRoleRepositoryImpl implements SysRoleRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
