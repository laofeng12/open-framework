package com.openjava.nhc.test.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ExampleOrderRepositoryImpl implements ExampleOrderRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
