package com.openjava.admin.logs.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class LogApiErrorRepositoryImpl implements LogApiErrorRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
