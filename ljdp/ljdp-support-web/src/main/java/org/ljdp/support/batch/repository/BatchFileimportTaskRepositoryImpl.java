package org.ljdp.support.batch.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BatchFileimportTaskRepositoryImpl implements BatchFileimportTaskRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
