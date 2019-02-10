package org.ljdp.core.db.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ljdp.component.paging.Page;
import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.GeneralDAO;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.db.SQLClause;
import org.ljdp.core.db.SQLUtil;
import org.ljdp.core.db.session.SessionManagerBean;
import org.ljdp.core.query.Parameter;

public class JPAGeneralDAO extends SessionManagerBean implements GeneralDAO {
	
	private Page page = null;
	
	public EntityManager getEm() {
		return (EntityManager)getSessionManager().getSession();
	}

	public void clear() {
		getEm().clear();
	}
	
	public void flush() {
		getEm().flush();
	}
	
	public void commit() {
		getEm().getTransaction().commit();
	}

	public Serializable create(Serializable vo) {
		getEm().persist(vo);
		return vo;
	}

	public void remove(Serializable vo) {
		getEm().remove(vo);
	}

	public void update(Serializable vo) {
		getEm().merge(vo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataPackage executeQuery(String sql, DBQueryParam param) {
		String[] sqls = SQLUtil.toPageSQL(sql, param);
		
		int rowcount = JPAQueryUtils.excuteCount(getEm(), sqls[0], new DBQueryParam[] {param});
		
		Collection mydatas = JPAQueryUtils.excuteQuery(getEm(), sqls[1], new DBQueryParam[] {param}, rowcount);
		
		DataPackage result = new DataPackage();
		result.setPageNo(param.get_pageno());
		result.setPageSize(param.get_pagesize());
		result.setTotalCount(rowcount);
		result.setDatas(mydatas);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql) {
		Query query = getEm().createNativeQuery(sql);
		configPaging(query);
		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Map<String, ?> map) {
		Query query = getEm().createNativeQuery(sql);
		JPAQueryUtils.bindParam(query, map);
		configPaging(query);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls) {
		Query query = getEm().createNativeQuery(sql, cls);
		configPaging(query);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls, Map<String, ?> map) {
		Query query = getEm().createNativeQuery(sql, cls);
		JPAQueryUtils.bindParam(query, map);
		configPaging(query);
		return query.getResultList();
	}
	
	public int updateNative(String sql) {
		Query query = getEm().createNativeQuery(sql);
		return query.executeUpdate();
	}
	
	public int updateNative(String sql, Map<String, ?> map) {
		Query query = getEm().createNativeQuery(sql);
		JPAQueryUtils.bindParam(query, map);
		return query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Collection<Parameter> params, int type) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, params);
		configPaging(query);
		if(DAO.OPER_QUERY == type) {
			return query.getResultList();
		} else if (DAO.OPER_DELETE == type || DAO.OPER_UPDATE == type) {
			int count = query.executeUpdate();
            ArrayList<Integer> alist = new ArrayList<Integer>(1);
            alist.add(new Integer(count));
            return alist;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Map<String, ?> map, int type) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, map);
		configPaging(query);
		if(DAO.OPER_QUERY == type) {
			return query.getResultList();
		} else if (DAO.OPER_DELETE == type || DAO.OPER_UPDATE == type) {
			int count = query.executeUpdate();
            ArrayList<Integer> alist = new ArrayList<Integer>(1);
            alist.add(new Integer(count));
            return alist;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List query(String sql) {
		Query query = getEm().createQuery(sql);
		configPaging(query);
		return query.getResultList();
	}

	@SuppressWarnings("rawtypes")
	public List query(String sql, Collection<Parameter> params) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, params);
		configPaging(query);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	public List query(String sql, Map<String, ?> map) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, map);
		configPaging(query);
		return query.getResultList();
	}

	public int update(String sql) {
		Query query = getEm().createQuery(sql);
		return query.executeUpdate();
	}

	public int update(String sql, Collection<Parameter> params) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, params);
		return query.executeUpdate();
	}
	
	public int update(String sql, Map<String, ?> map) {
		Query query = getEm().createQuery(sql);
		JPAQueryUtils.bindParam(query, map);
		return query.executeUpdate();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataPackage query(MultiTableQuery mtq, Integer queryType) throws Exception{
		if(null == mtq) {
			throw new IllegalArgumentException("the MultiTableQuery is null");
		}
		SQLClause hql = mtq.createSQLClause();
		
		DataPackage result = new DataPackage();
		result.setPageNo(mtq.getPageno());
		result.setPageSize(mtq.getPagesize());
		if(mtq.isInitPaging()) {
			mtq.getParamArray()[0].set_pageno(mtq.getPageno()+"");
			mtq.getParamArray()[0].set_pagesize(mtq.getPagesize()+"");
		}
		int rowcount = 0;
		/* 取记录数 */
		if (queryType.equals(DAO.QUERY_ALL) || queryType.equals(DAO.QUERY_COUNT)) {
			rowcount = JPAQueryUtils.excuteCount(getEm(), hql.buildCountSQL(), mtq.getParamArray());
			result.setTotalCount(rowcount);
		}
		
		/* 取指定页的数据 */
		if (queryType.equals(DAO.QUERY_ALL) || queryType.equals(DAO.QUERY_DATA)) {
			Collection mydatas = JPAQueryUtils.excuteQuery(getEm(), hql.buildSQL(), mtq.getParamArray(), rowcount);
			result.setDatas(mydatas);
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage query(MultiTableQuery mtq) throws Exception{
		return query(mtq, DAO.QUERY_ALL);
	}

	public void setPage(int firstResult, int maxResults) {
		page = new Page();
		page.setStart(firstResult);
		page.setLimit(maxResults);
	}

	void configPaging(Query query) {
		if(page != null) {
			query.setFirstResult(page.getStart());
			query.setMaxResults(page.getLimit());
		}
	}

	public void cancelPaging() {
		page = null;
	}
	
}
