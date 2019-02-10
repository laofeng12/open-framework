package com.openjava.example.file.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ExampleUploadFileRepositoryImpl implements ExampleUploadFileRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
