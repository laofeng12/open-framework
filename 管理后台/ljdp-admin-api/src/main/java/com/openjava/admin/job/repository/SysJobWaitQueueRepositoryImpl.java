package com.openjava.admin.job.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysJobWaitQueueRepositoryImpl implements SysJobWaitQueueRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
