package com.openjava.admin.batch.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BatchImportTaskRepositoryImpl implements BatchImportTaskRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
