package org.ljdp.core.spring.data;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import org.ljdp.core.db.RoDBQueryParam;
import org.ljdp.core.db.SQLClause;
import org.ljdp.core.db.SQLUtil;
import org.ljdp.core.db.jpa.JPAQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

public class JpaDynamicQueryDAO<T>  {
	
	private Class<T> domanClass;
	private EntityManager em;

	public JpaDynamicQueryDAO(EntityManager em, Class<T> domanClass) {
		this.em = em;
		this.domanClass = domanClass;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * 单表动态参数查询（查询条件不固定）
	 * 支持：分页+排序
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @param pageable 分页，例如： new PageRequest(0, 20);
	 * @return
	 */
	public Page<T> query(RoDBQueryParam params, Pageable pageable) {
		Assert.notNull(params);
		String alias = params.getTableAlias();
		try {
			SQLClause hql = createJPQLClause(params, pageable.getSort(), alias);
	        
	        int rowcount = JPAQueryUtils.excuteCount(em, hql.buildCountSQL(), params);
	        
	        List<T> list = JPAQueryUtils.excuteQuery(em, hql.buildSQL(), params, pageable);
	        
			Page<T> pageResult = new PageImpl<T>(list, pageable, rowcount);
			
			return pageResult;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}
	
	public List<T> queryDataOnly(RoDBQueryParam params, Pageable pageable) {
		Assert.notNull(params);
		String alias = params.getTableAlias();
		try {
			SQLClause hql = createJPQLClause(params, pageable.getSort(), alias);
	        
	        List<T> list = JPAQueryUtils.excuteQuery(em, hql.buildSQL(), params, pageable);
	        
			return list;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}

	/**
	 * 单表动态参数查询（查询条件不固定）
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public List<T> query(RoDBQueryParam params) {
		Sort sort = null;
		return query(params, sort);
	}
	
	/**
	 * 单表动态参数查询（查询条件不固定）
	 * 支持：排序
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @param sort 排序，例如：
	 * new Sort(new Sort.Order(Sort.Direction.DESC, "operTime"), 
	 * 			new Sort.Order(Sort.Direction.ASC, "orderChannel")
	 * 			)
	 * @return
	 */
	public List<T> query(RoDBQueryParam params, Sort sort) {
		Assert.notNull(params);
		String alias = params.getTableAlias();
		try {
			SQLClause hql = createJPQLClause(params, sort, alias);
			List<T> list = JPAQueryUtils.excuteQuery(em, hql.buildSQL(), params, null);
			return list;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}
	
	/**
	 * 单表动态参数总数计算（查询条件不固定）
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public int count(RoDBQueryParam params) {
		Assert.notNull(params);
		String alias = params.getTableAlias();
		try {
			SQLClause hql = createJPQLClause(params, null, alias);
			int rowcount = JPAQueryUtils.excuteCount(em, hql.buildCountSQL(), params);
			return rowcount;
		} catch (Exception e) {
			throw new DynamicParamSQLException(e);
		}
	}

	private SQLClause createJPQLClause(RoDBQueryParam params, Sort sort,
			String alias) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		SQLClause hql = new SQLClause();
		SQLUtil.expressClause(hql, domanClass, params, alias);
		
		if(sort != null) {
			Iterator<Sort.Order> it = sort.iterator();
			while (it.hasNext()) {
				Sort.Order order = (Sort.Order) it.next();
				hql.addOrderBy(order.getProperty(), order.getDirection().toString(), alias);
			}
		}
		return hql;
	}
	
}
