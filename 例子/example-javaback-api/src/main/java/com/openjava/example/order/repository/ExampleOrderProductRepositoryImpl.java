package com.openjava.example.order.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ExampleOrderProductRepositoryImpl implements ExampleOrderProductRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
