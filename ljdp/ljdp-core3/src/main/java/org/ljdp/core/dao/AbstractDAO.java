package org.ljdp.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.db.session.SessionManager;
import org.ljdp.core.query.Parameter;

/**
 * AbstractDAO, 将dao行为委托到具体的DAO实现类，以便根据配置灵活切换不同的OR框架
 * @author Hzy
 *
 */
public abstract class AbstractDAO<T> implements DAO<T> {
	private Class<T> voClass;
	
	private DAO<T> delegateDAO;
	
	public AbstractDAO() {
		this(null);
	}
	
	public AbstractDAO(Class<T> voClass) {
		this.voClass = voClass;
	}

	public Class<T> getEntityClass() {
		return voClass;
	}

	public void setEntityClass(Class<T> voClass) {
		this.voClass = voClass;
	}

	public DAO<T> getDelegateDAO() {
		return delegateDAO;
	}

	public void setDelegateDAO(DAO<T> delegateDAO) {
		this.delegateDAO = delegateDAO;
		this.delegateDAO.setEntityClass(voClass);
	}

	public void clear(Object vo) {
		delegateDAO.clear(vo);
	}

	public void clear() {
		delegateDAO.clear();
	}
	
	public void flush() {
		delegateDAO.flush();
	}
	
	public void commit() {
		delegateDAO.commit();
	}

	public Serializable create(Serializable vo) {
		return delegateDAO.create(vo);
	}

	public Collection<T> findAll() {
		return delegateDAO.findAll();
	}

	public T findByLongPK(Long pk) {
		return delegateDAO.findByLongPK(pk);
	}

	public T findByPK(Serializable pk) {
		return delegateDAO.findByPK(pk);
	}

	public T findByVOPK(T vo) {
		return delegateDAO.findByVOPK(vo);
	}

	public T findFirstByProperty(String proName, Object value) {
		return delegateDAO.findFirstByProperty(proName, value);
	}

	public T findFirstByProperty(String proName, Object value,
			boolean sessionCache) {
		return delegateDAO.findFirstByProperty(proName, value, sessionCache);
	}

	public Number getMaxValue(String prop) {
		return delegateDAO.getMaxValue(prop);
	}

	public Number getMaxValue(String prop, DBQueryParam params)
			throws Exception {
		return delegateDAO.getMaxValue(prop, params);
	}

	public SessionManager getSessionManager() {
		return delegateDAO.getSessionManager();
	}

	public DataPackage<T> query(DBQueryParam params) throws Exception {
		return delegateDAO.query(params);
	}

	public DataPackage<T> query(DBQueryParam params, Integer queryType)
			throws Exception {
		return delegateDAO.query(params, queryType);
	}

	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams) throws Exception {
		return delegateDAO.query(params, joinClause, joinParams);
	}

	public DataPackage<T> query(DBQueryParam params, String joinClause,
			DBQueryParam joinParams, Integer queryType) throws Exception {
		return delegateDAO.query(params, joinClause, joinParams, queryType);
	}

	@SuppressWarnings("rawtypes")
	public DataPackage queryByNameSQL(String name, DBQueryParam params) {
		return delegateDAO.queryByNameSQL(name, params);
	}

	@SuppressWarnings("rawtypes")
	public DataPackage queryByNameSQL(String name, DBQueryParam params,
			Integer queryType) {
		return delegateDAO.queryByNameSQL(name, params, queryType);
	}
	
	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Collection<Parameter> params, int type) {
		return delegateDAO.execute(sql, params, type);
	}
	
	@SuppressWarnings("rawtypes")
	public List query(String sql) {
		return delegateDAO.query(sql);
	}
	
	@SuppressWarnings("rawtypes")
	public List query(String sql, Collection<Parameter> params) {
		return delegateDAO.query(sql, params);
	}
	
	public int update(String sql) {
		return delegateDAO.update(sql);
	}
	
	public int update(String sql, Collection<Parameter> params) {
		return delegateDAO.update(sql, params);
	}
	
	@SuppressWarnings("rawtypes")
	public DataPackage executeQuery(String sql, DBQueryParam param) {
		return delegateDAO.executeQuery(sql, param);
	}

	public void remove(Serializable vo) {
		delegateDAO.remove(vo);
	}

	public void removeByPK(Serializable pk) {
		delegateDAO.removeByPK(pk);
	}

	public void removeByVOPK(T vo) {
		delegateDAO.removeByVOPK(vo);
	}

	public void setSessionManager(SessionManager sessionManager) {
		delegateDAO.setSessionManager(sessionManager);
	}

	public void update(Serializable vo) {
		delegateDAO.update(vo);
	}

	@SuppressWarnings("rawtypes")
	public List query(String sql, Map<String, ?> map) {
		return delegateDAO.query(sql, map);
	}

	public int update(String sql, Map<String, ?> map) {
		return delegateDAO.update(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public List queryNative(String sql) {
		return delegateDAO.queryNative(sql);
	}

	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Map<String, ?> map) {
		return delegateDAO.queryNative(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls) {
		return delegateDAO.queryNative(sql, cls);
	}

	@SuppressWarnings("rawtypes")
	public List queryNative(String sql, Class cls, Map<String, ?> map) {
		return delegateDAO.queryNative(sql, cls, map);
	}

	public int updateNative(String sql) {
		return delegateDAO.updateNative(sql);
	}

	public int updateNative(String sql, Map<String, ?> map) {
		return delegateDAO.updateNative(sql, map);
	}

	@SuppressWarnings("rawtypes")
	public Collection execute(String sql, Map<String, ?> map, int type) {
		return delegateDAO.execute(sql, map, type);
	}

	@SuppressWarnings("rawtypes")
	public List queryEntityNative(String sql) {
		return delegateDAO.queryEntityNative(sql);
	}

	@SuppressWarnings("rawtypes")
	public List queryEntityNative(String sql, Map<String, ?> map) {
		return delegateDAO.queryEntityNative(sql, map);
	}

	protected Session currentSession() {
		return (Session)getSessionManager().getSession();
	}

	public void setPage(int firstResult, int maxResults) {
		delegateDAO.setPage(firstResult, maxResults);
	}
	public void cancelPaging() {
		delegateDAO.cancelPaging();
	}

	@SuppressWarnings("rawtypes")
	public DataPackage query(MultiTableQuery mtq, Integer queryType)
			throws Exception {
		return delegateDAO.query(mtq, queryType);
	}

	@SuppressWarnings("rawtypes")
	public DataPackage query(MultiTableQuery mtq) throws Exception {
		return delegateDAO.query(mtq);
	}
}
