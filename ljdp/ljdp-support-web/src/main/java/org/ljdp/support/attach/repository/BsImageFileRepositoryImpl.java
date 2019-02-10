package org.ljdp.support.attach.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BsImageFileRepositoryImpl implements BsImageFileRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
