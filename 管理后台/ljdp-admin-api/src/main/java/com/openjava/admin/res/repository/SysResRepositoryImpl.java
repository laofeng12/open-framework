package com.openjava.admin.res.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysResRepositoryImpl implements SysResRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
