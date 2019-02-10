package org.ljdp.core.spring.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class DynamicJpaRepositoryImpl<T, ID extends Serializable> 
			extends SimpleJpaRepository<T, ID> 
			implements DynamicJpaRepository<T, ID>{

//	private EntityManager entityManager;
//	private Class<T> domainClass;
	protected JpaDynamicQueryDAO<T> dynamicDao;

	// There are two constructors to choose from, either can be used.
	public DynamicJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);

		// This is the recommended method for accessing inherited class
		// dependencies.
//		this.domainClass = domainClass;
//		this.entityManager = entityManager;
		
		dynamicDao = new JpaDynamicQueryDAO<T>(entityManager, domainClass);
	}

	public Page<T> query(RoDBQueryParam params, Pageable pageable) {
		return dynamicDao.query(params, pageable);
	}
	
	public List<T> queryDataOnly(RoDBQueryParam params, Pageable pageable) {
		return dynamicDao.queryDataOnly(params, pageable);
	}
	
	public List<T> query(RoDBQueryParam params) {
		return dynamicDao.query(params);
	}
	
	public List<T> query(RoDBQueryParam params, Sort sort) {
		return dynamicDao.query(params, sort);
	}
	
	public int count(RoDBQueryParam params) {
		return dynamicDao.count(params);
	}
	
	public JpaDynamicQueryDAO<T> getDynamicDao() {
		return dynamicDao;
	}

}
