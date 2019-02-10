package org.ljdp.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.DAOFactory;
import org.ljdp.core.dao.GeneralDAO;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.query.Parameter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通用的服务提供者(ServiceProvide)
 * 相当于无状态的EJB bean
 * @author Hzy
 *
 */
@SuppressWarnings("rawtypes")
@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
public class GeneralSP extends AbstractService implements GeneralService {
	
	private GeneralDAO dao;

	public GeneralDAO getCurrentDAO() {
		if(dao == null) {
			dao = DAOFactory.build(getUserAccessDB());
		}
		return dao;
	}
	
	public void setPage(int firstResult, int maxResults) {
		getCurrentDAO().setPage(firstResult, maxResults);
	}
	public void cancelPaging() {
		getCurrentDAO().cancelPaging();
	}

	public Serializable doCreate(Serializable vo) throws Exception {
		return getCurrentDAO().create(vo);
	}
	
	public void doBatchCreate(List<Serializable> list) throws Exception{
		for (Serializable vo : list) {
			getCurrentDAO().create(vo);
		}
	}

	public void doRemove(Serializable vo) throws Exception {
		getCurrentDAO().remove(vo);
	}

	public void doUpdate(Serializable vo) throws Exception {
		getCurrentDAO().update(vo);
	}

	public List doQuery(String sql) {
		return getCurrentDAO().query(sql);
	}

	public List doQuery(String sql, Collection<Parameter> params) {
		return getCurrentDAO().query(sql, params);
	}

	public int doUpdate(String sql) throws Exception {
		return getCurrentDAO().update(sql);
	}

	public int doUpdate(String sql, Collection<Parameter> params)
			throws Exception {
		return getCurrentDAO().update(sql, params);
	}

	public DataPackage doExecuteQuery(String sql, DBQueryParam param) {
		return getCurrentDAO().executeQuery(sql, param);
	}

	public Object getSingleResult(String sql) {
		List list = getCurrentDAO().query(sql);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Object getSingleResult(String sql, Collection<Parameter> params) {
		List list = getCurrentDAO().query(sql, params);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List doQuery(String sql, Map<String, ?> map) {
		return getCurrentDAO().query(sql, map);
	}

	public Object getSingleResult(String sql, Map<String, ?> map) {
		List list = getCurrentDAO().query(sql, map);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public int doUpdate(String sql, Map<String, ?> map) throws Exception {
		return getCurrentDAO().update(sql, map);
	}

	public List doQueryNative(String sql) {
		return getCurrentDAO().queryNative(sql);
	}

	public List doQueryNative(String sql, Map<String, ?> map) {
		return getCurrentDAO().queryNative(sql, map);
	}

	public List doQueryNative(String sql, Class cls) {
		return getCurrentDAO().queryNative(sql, cls);
	}

	public List doQueryNative(String sql, Class cls, Map<String, ?> map) {
		return getCurrentDAO().queryNative(sql, map);
	}

	public int doUpdateNative(String sql) {
		return getCurrentDAO().updateNative(sql);
	}

	public int doUpdateNative(String sql, Map<String, ?> map) {
		return getCurrentDAO().updateNative(sql, map);
	}

	public Object doTransaction(BusinessObject strategy, Object[] params)
			throws Exception {
		return strategy.doBusiness(params);
	}

	public Object doTransaction(BusinessObject strategy) throws Exception {
		return strategy.doBusiness();
	}

	public DataPackage doQuery(MultiTableQuery mtq, Integer queryType)
			throws Exception {
		return getCurrentDAO().query(mtq, queryType);
	}

	public DataPackage doQuery(MultiTableQuery mtq) throws Exception {
		return getCurrentDAO().query(mtq);
	}

	public int doCount(MultiTableQuery mtq) throws Exception {
		DataPackage dp = getCurrentDAO().query(mtq, DAO.QUERY_COUNT);
		return dp.getTotalCount();
	}
	
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
	
	public Object doQuerySingleNative(String sql) {
		List list = getCurrentDAO().queryNative(sql);
		if( list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
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
	
	public void clear() {
		getCurrentDAO().clear();
	}
	
	public void flush() {
		getCurrentDAO().flush();
	}
}
