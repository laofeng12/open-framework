package org.ljdp.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;

public interface CommonService<T> extends GeneralService{
	
	public void doRemoveByPK(Serializable pk) throws Exception;
	
	public void doRemoveByVOPK(T vo) throws Exception;
	
	public T doFindFirstByProperty(String proName, Object value);
	
	public T doFindByLongPK(Long pk);
	
	public T doFindByPK(Serializable pk);
	
	public Collection<T> doFindAll();
	
	public Number doGetMaxValue(String prop);
	
	public Number doGetMaxValue(String prop, DBQueryParam params) throws Exception;
	
	public int doCount(DBQueryParam params) throws Exception;
	
	public Collection<T> doDatas(DBQueryParam params) throws Exception;
	
	public List<T> doList(DBQueryParam params) throws Exception;
	
	public DataPackage<T> doQuery(DBQueryParam params) throws Exception;
	
	public DataPackage<T> doQuery(DBQueryParam params, Integer queryType) throws Exception;
	
	public DataPackage<T> doQuery(DBQueryParam params, String joinClause,
            DBQueryParam joinParams, int queryType) throws Exception;
	
	public T doQuerySingle(DBQueryParam params) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQueryByNameSQL(String name, DBQueryParam params) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQueryDataByNameSQL(String name, DBQueryParam params) throws Exception;

	public Class<T> getEntityClass();

	public void setEntityClass(Class<T> voClass);

	public DataPackage<T> doQueryAll(DBQueryParam params) throws Exception;
	
	public DataPackage<T> doQueryData(DBQueryParam params) throws Exception;
	
	public Object doTransaction(BusinessObject strategy, Object[] params) throws Exception;
	
	public Object doTransaction(BusinessObject strategy) throws Exception;
	
	public T getSingleResult(DBQueryParam params) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public List doQueryEntityNative(String sql);
	@SuppressWarnings("rawtypes")
	public List doQueryEntityNative(String sql, Map<String, ?> map);
	
	public Class<?> getDaoClass();

	public void setDaoClass(Class<?> daoClass);
	
}
