package org.ljdp.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.query.Parameter;

public interface GeneralService extends Authorizable {
	public Serializable doCreate(Serializable vo) throws Exception;
	public void doBatchCreate(List<Serializable> list) throws Exception;
	
	public void doRemove(Serializable vo) throws Exception;
	
	public void doUpdate(Serializable vo) throws Exception;

	@SuppressWarnings("rawtypes")
	public List doQuery(String sql);

	@SuppressWarnings("rawtypes")
	public List doQuery(String sql, Map<String,?> map);
	
	@SuppressWarnings("rawtypes")
	public List doQuery(String sql, Collection<Parameter> params);
	
	public int doUpdate(String sql) throws Exception;
	
	public int doUpdate(String sql, Collection<Parameter> params) throws Exception;

	@SuppressWarnings("rawtypes")
	public DataPackage doExecuteQuery(String sql, DBQueryParam param);
	
	public Object getSingleResult(String sql);
	public Object getSingleResult(String sql, Collection<Parameter> params);
	public Object getSingleResult(String sql, Map<String,?> map);
	
	public Object doQuerySingle(String sql);
	public Object doQuerySingle(String sql, Map<String,?> map);
	
	public Object doQuerySingleNative(String sql);
	public Object doQuerySingleNative(String sql, Map<String,?> map);
	
	public int doUpdate(String sql, Map<String,?> map) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql);
	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Map<String, ?> map);
	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Class cls);
	@SuppressWarnings("rawtypes")
	public List doQueryNative(String sql, Class cls, Map<String, ?> map);
	
	public int doUpdateNative(String sql);
	public int doUpdateNative(String sql, Map<String, ?> map);	
	
	public Object doTransaction(BusinessObject strategy, Object[] params) throws Exception;
	public Object doTransaction(BusinessObject strategy) throws Exception;
	
	@SuppressWarnings("rawtypes")
	public DataPackage doQuery(MultiTableQuery mtq, Integer queryType) throws Exception;
	@SuppressWarnings("rawtypes")
	public DataPackage doQuery(MultiTableQuery mtq) throws Exception;
	
	public int doCount(MultiTableQuery mtq) throws Exception;
	@SuppressWarnings("rawtypes")
	public Collection doDatas(MultiTableQuery mtq) throws Exception;
	
	public int doCount(String sql);
	public int doCount(String sql, Collection<Parameter> params);
	public int doCount(String sql, Map<String, ?> map);
	public int doCountNative(String sql);
	public int doCountNative(String sql, Map<String, ?> map);
	
	public void setPage(int firstResult, int maxResults);
	public void cancelPaging();
	
	public void clear();
	public void flush();
}
