package com.openjava.admin.org.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysOrgRepositoryImpl implements SysOrgRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
