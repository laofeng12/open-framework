package org.ljdp.core.db.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.core.dao.BaseEntityDAO;
import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.GeneralDAO;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.db.SQLClause;
import org.ljdp.core.db.SQLUtil;
import org.ljdp.core.query.Parameter;

public class JPABaseDAO<T> extends BaseEntityDAO<T> implements DAO<T> {
	
	private GeneralDAO dao;
	
	public JPABaseDAO() {
		super();
	}
	public JPABaseDAO(Class<T> voClass) {
		super(voClass);
	}
	
	protected GeneralDAO getGeneralDAO() {
		if(dao == null) {
			dao = new JPAGeneralDAO();
			dao.setSessionManager(getSessionManager());
		}
		return dao;
	}

	public void clear(Object vo) {

	}

	public void clear() {
		getGeneralDAO().clear();
	}

	public void flush() {
		getEm().flush();
	}
	
	public void commit() {
		getEm().getTransaction().commit();
	}

	public Serializable create(Serializable vo) {
		return getGeneralDAO().create(vo);
	}

	public void remove(Serializable vo) {
		getGeneralDAO().remove(vo);
	}

	public void removeByPK(Serializable pk) {
		Object entity = getEm().find(entityClass, pk);
		if (entity != null) {
			getEm().remove(entity);
		}
	}

	public void removeByVOPK(T vo) {
		throw new RuntimeException("此功能暂未开放");
	}

	public void update(Serializable vo) {
		getGeneralDAO().update(vo);
	}

	@SuppressWarnings("unchecked")
	public T findFirstByProperty(String proName, Object value) {
		 String hql = SQLUtil.buildFindQuery(entityClass.getName(), new String[] {proName});
		 Query query = getEm().createQuery(hql);
		 query.setParameter(1, value);
		 List<T> list = query.getResultList();
		 if(list.size() > 0) {
			 return list.get(0);
		 }
		return null;
	}

	public T findFirstByProperty(String proName, Object value,
			boolean sessionCache) {
		return findFirstByProperty(proName, value);
	}

	public T findByLongPK(Long pk) {
		throw new RuntimeException("此功能暂未开放");
	}

	public T findByPK(Serializable pk) {
		return getEm().find(getEntityClass(), pk);
	}

	public T findByVOPK(T vo) {
		throw new RuntimeException("此功能暂未开放");
	}

	@SuppressWarnings("unchecked")
	public Collection<T> findAll() {
		String hql = "from " + entityClass.getName();
		Query query = getEm().createQuery(hql);
		return query.getResultList();
	}

	public Number getMaxValue(String prop) {
		if (StringUtils.isNotBlank(prop)) {
			StringBuffer sql = new StringBuffer("SELECT max(this.")
			.append(prop).append(") FROM ").append(entityClass.getName())
			.append(" this");
			Query query = getEm().createQuery(sql.toString());
			return (Number)query.getSingleResult();
		}
		return null;
	}

	public Number getMaxValue(String prop, DBQueryParam params)
			throws Exception {
		if(StringUtils.isBlank(prop) || params == null) {
    		return null;
    	}
    	SQLClause sql = new SQLClause();
    	sql.getSelect().append("max(").append(alias[0])
    		.append(".").append(prop).append(") ");
    	sql.addFromTable(entityClass.getName(), alias[0]);
    	sql.addWhere( SQLUtil.buildWhereSQL(params, alias[0]) );
    	Query query = getEm().createQuery(sql.buildSQL());
    	JPAQueryUtils.bindParam(query, params.getAllParam());
		return (Number)query.getSingleResult();
	}

	public DataPackage<T> query(DBQueryParam params) throws Exception {
		return query(params, QUERY_ALL);
	}

	public DataPackage<T> query(DBQueryParam params, Integer queryType)
			throws Exception {
		return query(params, null, null, queryType);  
	}

	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams) throws Exception {
		return query(params, joinClause, joinParams, QUERY_ALL);
	}

	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams, Integer queryType) throws Exception {
		if(null == params) {
			throw new IllegalArgumentException("the DBQueryParam is null");
		}
		SQLClause hql = createSQLClause(params);
        
        /* 判断是否需要进行两个表的联合查询 */
        DBQueryParam[] paramsArray = parseDBQueryParam(params, joinClause,
				joinParams, hql);
        
        JPAPackageQuery<T> queryer = new JPAPackageQuery<T>();
        DataPackage<T> result = queryer.excuteHQL(getEm(), hql, paramsArray, queryType);
        wrapDataPackage(result.toList(), paramsArray);
        
		return result;
	}

	@SuppressWarnings("rawtypes")
	public DataPackage queryByNameSQL(String name, DBQueryParam params) {
		throw new RuntimeException("此功能暂未开放");
	}

	@SuppressWarnings("rawtypes")
	public DataPackage queryByNameSQL(String name, DBQueryParam params,
			Integer queryType) {
		throw new RuntimeException("此功能暂未开放");
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage executeQuery(String sql, DBQueryParam param) {
		return getGeneralDAO().executeQuery(sql, param);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql) {
		return getGeneralDAO().queryNative(sql);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Map<String, ?> map) {
		return getGeneralDAO().queryNative(sql, map);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls) {
		return getGeneralDAO().queryNative(sql, cls);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls, Map<String, ?> map) {
		return getGeneralDAO().queryNative(sql, cls, map);
	}
	
	@SuppressWarnings("rawtypes")
	public List queryEntityNative(String sql) {
		return queryNative(sql, getEntityClass());
	}
	
	@SuppressWarnings("rawtypes")
	public List queryEntityNative(String sql, Map<String, ?> map) {
		return queryNative(sql, getEntityClass(), map);
	}
	
	public int updateNative(String sql) {
		return getGeneralDAO().updateNative(sql);
	}
	
	public int updateNative(String sql, Map<String, ?> map) {
		return getGeneralDAO().updateNative(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Collection<Parameter> params, int type) {
		return getGeneralDAO().execute(sql, params, type);
	}
	
	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Map<String, ?> map, int type) {
		return getGeneralDAO().execute(sql, map, type);
	}

	@SuppressWarnings("rawtypes")
	public List query(String sql) {
		return getGeneralDAO().query(sql);
	}

	@SuppressWarnings("rawtypes")
	public List query(String sql, Collection<Parameter> params) {
		return getGeneralDAO().query(sql, params);
	}
	
	@SuppressWarnings("rawtypes")
	public List query(String sql, Map<String, ?> map) {
		return getGeneralDAO().query(sql, map);
	}

	public int update(String sql) {
		return getGeneralDAO().update(sql);
	}

	public int update(String sql, Collection<Parameter> params) {
		return getGeneralDAO().update(sql, params);
	}
	
	public int update(String sql, Map<String, ?> map) {
		return getGeneralDAO().update(sql, map);
	}
	
	public void setPage(int firstResult, int maxResults) {
		getGeneralDAO().setPage(firstResult, maxResults);
	}
	
	public EntityManager getEm() {
		return (EntityManager)getSessionManager().getSession();
	}
	public void cancelPaging() {
		getGeneralDAO().cancelPaging();
	}
	@SuppressWarnings("rawtypes")
	public DataPackage query(MultiTableQuery mtq, Integer queryType)
			throws Exception {
		return getGeneralDAO().query(mtq, queryType);
	}
	@SuppressWarnings("rawtypes")
	public DataPackage query(MultiTableQuery mtq) throws Exception {
		return getGeneralDAO().query(mtq);
	}

}
