package com.openjava.framework.sys.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class LmMemberTokenRepositoryImpl implements LmMemberTokenRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
