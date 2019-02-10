package org.ljdp.core.spring.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class MultiDynamicJpaRepositoryImpl<T, ID extends Serializable>  
		extends DynamicJpaRepositoryImpl<T, ID>
		implements MultiDynamicJpaRepository<T, ID>{

	protected JpaMultiDynamicQueryDAO multiDynamicDao;
	
	public MultiDynamicJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);

		// This is the recommended method for accessing inherited class
		// dependencies.
//		this.domainClass = domainClass;
//		this.entityManager = entityManager;
		multiDynamicDao = new JpaMultiDynamicQueryDAO(entityManager);
	}

	public Page<?> query(String jpql, Pageable pageable , RoDBQueryParam... paramArray){
		return multiDynamicDao.query(jpql, pageable, paramArray);
	}
	
	public List<?> query(String jpql, Sort sort, RoDBQueryParam... paramArray){
		return multiDynamicDao.query(jpql, sort, paramArray);
	}
	
	public List<?> query(String jpql, RoDBQueryParam... paramArray){
		return multiDynamicDao.query(jpql, paramArray);
	}
	
	public int count(String jpql, RoDBQueryParam... paramArray){
		return multiDynamicDao.count(jpql, paramArray);
	}

	public JpaMultiDynamicQueryDAO getMultiDynamicDao() {
		return multiDynamicDao;
	}

}
