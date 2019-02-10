package org.ljdp.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.DAOFactory;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.query.Parameter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通用的服务提供者(ServiceProvide)
 * 相当于有状态的EJB bean
 * @author Hzy
 *
 */
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class CommonSP<T> extends AbstractService implements CommonService<T> {
	private Class<T> entityClass;
	private Class<?> daoClass;
	private DAO<T> dao;
	
	public CommonSP() {
	}
	
	public CommonSP(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public CommonSP(Class<T> entityClass, Class<?> daoClass) {
		this.entityClass = entityClass;
		this.daoClass = daoClass;
	}

	public Serializable doCreate(Serializable vo) throws Exception{
		DAO<T> dao = getCurrentDAO();
		return dao.create(vo);
	}
	
	public void doBatchCreate(List<Serializable> list) throws Exception{
		for (Serializable vo : list) {
			getCurrentDAO().create(vo);
		}
	}

	public Collection<T> doFindAll() {
		DAO<T> dao = getCurrentDAO();
		return dao.findAll();
	}

	public T doFindByLongPK(Long pk) {
		DAO<T> dao = getCurrentDAO();
		return dao.findByLongPK(pk);
	}

	public T doFindByPK(Serializable pk) {
		DAO<T> dao = getCurrentDAO();
		return dao.findByPK(pk);
	}

	public T doFindFirstByProperty(String proName, Object value) {
		DAO<T> dao = getCurrentDAO();
		return dao.findFirstByProperty(proName, value);
	}

	public Number doGetMaxValue(String prop) {
		DAO<T> dao = getCurrentDAO();
		return dao.getMaxValue(prop);
	}

	public Number doGetMaxValue(String prop, DBQueryParam params)
			throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.getMaxValue(prop, params);
	}
	
	public int doCount(DBQueryParam params) throws Exception {
		DAO<T> dao = getCurrentDAO();
		DataPackage<T> dp = dao.query(params, DAO.QUERY_COUNT);
		return dp.getTotalCount();
	}
	
	public Collection<T> doDatas(DBQueryParam params) throws Exception{
		DAO<T> dao = getCurrentDAO();
		DataPackage<T> dp = dao.query(params, DAO.QUERY_DATA);
		return dp.getDatas();
	}
	
	public List<T> doList(DBQueryParam params) throws Exception{
		DAO<T> dao = getCurrentDAO();
		DataPackage<T> dp = dao.query(params, DAO.QUERY_DATA);
		return dp.toList();
	}

	public DataPackage<T> doQuery(DBQueryParam params) throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.query(params);
	}
	
	public DataPackage<T> doQuery(DBQueryParam params, Integer queryType)
			throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.query(params, queryType);
	}

	public DataPackage<T> doQueryData(DBQueryParam params) throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.query(params, DAO.QUERY_DATA);
	}

	public DataPackage<T> doQueryAll(DBQueryParam params) throws Exception {
		params.set_pageno("1");
		if(!params.getRestrictQueryQuantity()) {
			params.set_pagesize(""+Integer.MAX_VALUE);
		}
		return doQuery(params, DAO.QUERY_DATA);
	}

	public DataPackage<T> doQuery(DBQueryParam params, String joinClause,
			DBQueryParam joinParams, int queryType) throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.query(params, joinClause, joinParams, queryType);
	}
	
	public T doQuerySingle(DBQueryParam params) throws Exception{
		DAO<T> dao = getCurrentDAO();
		DataPackage<T> dp = dao.query(params, DAO.QUERY_DATA);
		Iterator<T> it = dp.getDatas().iterator();
		if(it.hasNext()) {
			return it.next();
		}
		return null;
	}

	public void doRemove(Serializable vo) throws Exception{
		DAO<T> dao = getCurrentDAO();
		dao.remove(vo);
	}

	public void doRemoveByPK(Serializable pk) throws Exception{
		DAO<T> dao = getCurrentDAO();
		dao.removeByPK(pk);
	}

	public void doRemoveByVOPK(T vo) throws Exception{
		DAO<T> dao = getCurrentDAO();
		dao.removeByVOPK(vo);
	}

	public void doUpdate(Serializable vo) throws Exception{
		DAO<T> dao = getCurrentDAO();
		dao.update(vo);
	}

	@SuppressWarnings("rawtypes")
	public DataPackage doQueryByNameSQL(String name, DBQueryParam params)
			throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.queryByNameSQL(name, params);
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQueryDataByNameSQL(String name, DBQueryParam params)
			throws Exception {
		DAO<T> dao = getCurrentDAO();
		return dao.queryByNameSQL(name, params, DAO.QUERY_DATA);
	}
	
	public Object doTransaction(BusinessObject strategy, Object[] params) throws Exception{
		return strategy.doBusiness(params);
	}
	
	public Object doTransaction(BusinessObject strategy) throws Exception{
		return strategy.doBusiness();
	}
	
	@SuppressWarnings("rawtypes")
	public List doQuery(String sql) {
		DAO<T> dao = getCurrentDAO();
		return dao.query(sql);
	}
	
	@SuppressWarnings("rawtypes")
	public List doQuery(String sql, Collection<Parameter> params) {
		DAO<T> dao = getCurrentDAO();
		return dao.query(sql, params);
	}
	
	@SuppressWarnings("rawtypes")
	public List doQuery(String sql, Map<String,?> map) {
		DAO<T> dao = getCurrentDAO();
		return dao.query(sql, map);
	}
	
	public T getSingleResult(DBQueryParam params) throws Exception{
		Collection<T> colls = doDatas(params);
		Iterator<T> it = colls.iterator();
		if(it.hasNext()) {
			return it.next();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getSingleResult(String sql) {
		List list = doQuery(sql);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public Object getSingleResult(String sql, Collection<Parameter> params) {
		List list = doQuery(sql, params);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Object getSingleResult(String sql, Map<String,?> map) {
		List list = doQuery(sql, map);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public int doUpdate(String sql) throws Exception{
		try {
			DAO<T> dao = getCurrentDAO();
			return dao.update(sql);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	public int doUpdate(String sql, Collection<Parameter> params) throws Exception{
		try {
			DAO<T> dao = getCurrentDAO();
			return dao.update(sql, params);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	public int doUpdate(String sql, Map<String,?> map) throws Exception{
		try {
			DAO<T> dao = getCurrentDAO();
			return dao.update(sql, map);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage doExecuteQuery(String sql, DBQueryParam param) {
		DAO dao = getCurrentDAO();
		return dao.executeQuery(sql, param);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql) {
		DAO dao = getCurrentDAO();
		return dao.queryNative(sql);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Map<String, ?> map) {
		DAO<T> dao = getCurrentDAO();
		return dao.queryNative(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Class cls) {
		DAO dao = getCurrentDAO();
		return dao.queryNative(sql, cls);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Class cls, Map<String, ?> map) {
		DAO<T> dao = getCurrentDAO();
		return dao.queryNative(sql, cls, map);
	}

	public int doUpdateNative(String sql) {
		DAO<T> dao = getCurrentDAO();
		return dao.updateNative(sql);
	}

	public int doUpdateNative(String sql, Map<String, ?> map) {
		DAO<T> dao = getCurrentDAO();
		return dao.updateNative(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryEntityNative(String sql) {
		DAO dao = getCurrentDAO();
		return dao.queryEntityNative(sql);
	}

	@SuppressWarnings("rawtypes")
	public List doQueryEntityNative(String sql, Map<String, ?> map) {
		DAO<T> dao = getCurrentDAO();
		return dao.queryEntityNative(sql, map);
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQuery(MultiTableQuery mtq, Integer queryType)
		throws Exception {
		return getCurrentDAO().query(mtq, queryType);
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQuery(MultiTableQuery mtq) throws Exception {
		return getCurrentDAO().query(mtq);
	}
	
	@SuppressWarnings("rawtypes")
	public int doCount(MultiTableQuery mtq) throws Exception {
		DataPackage dp = getCurrentDAO().query(mtq, DAO.QUERY_COUNT);
		return dp.getTotalCount();
	}
	
	@SuppressWarnings("rawtypes")
	public Collection doDatas(MultiTableQuery mtq) throws Exception{
		DataPackage dp = getCurrentDAO().query(mtq, DAO.QUERY_DATA);
		return dp.getDatas();
	}

	public Object doQuerySingle(String sql) {
		return getSingleResult(sql);
	}
	public Object doQuerySingle(String sql, Map<String,?> map) {
		return getSingleResult(sql, map);
	}
	
	@SuppressWarnings("rawtypes")
	public Object doQuerySingleNative(String sql) {
		List list = getCurrentDAO().queryNative(sql);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	public Object doQuerySingleNative(String sql, Map<String,?> map) {
		List list = getCurrentDAO().queryNative(sql, map);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	private int toInteger(Number num) {
		if(num != null) {
			return num.intValue();
		}
		return 0;
	}
	public int doCount(String sql) {
		Number num = (Number)getSingleResult(sql);
		return toInteger(num);
	}
	public int doCount(String sql, Collection<Parameter> params) {
		Number num = (Number)getSingleResult(sql, params);
		return toInteger(num);
	}
	public int doCount(String sql, Map<String, ?> map) {
		Number num = (Number)getSingleResult(sql, map);
		return toInteger(num);
	}
	public int doCountNative(String sql) {
		Number num = (Number)doQuerySingleNative(sql);
		return toInteger(num);
	}
	public int doCountNative(String sql, Map<String, ?> map) {
		Number num = (Number)doQuerySingleNative(sql, map);
		return toInteger(num);
	}
	
	@SuppressWarnings("unchecked")
	public DAO<T> getCurrentDAO() {
		if(dao == null) {
			if(getDaoClass() != null) {
				dao = DAOFactory.build(getDaoClass(), getUserAccessDB());
			} else {
				dao = DAOFactory.buildCommon(getEntityClass(), getUserAccessDB());
			}
		}
		return dao;
	}
	
	public void setPage(int firstResult, int maxResults) {
		getCurrentDAO().setPage(firstResult, maxResults);
	}
	public void cancelPaging() {
		getCurrentDAO().cancelPaging();
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<T> voClass) {
		this.entityClass = voClass;
	}

	public Class<?> getDaoClass() {
		return daoClass;
	}

	public void setDaoClass(Class<?> daoClass) {
		this.daoClass = daoClass;
	}
	
	public void clear() {
		getCurrentDAO().clear();
	}

	public void flush() {
		getCurrentDAO().flush();
	}
}
