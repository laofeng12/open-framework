package com.openjava.admin.sys.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SysNoticeRepositoryImpl implements SysNoticeRepositoryCustom {
	private EntityManager em;
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
}
