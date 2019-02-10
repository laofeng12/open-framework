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

@SuppressWarnings("rawtypes")
public abstract class AbstractGeneralDAO implements GeneralDAO {
	
	private GeneralDAO delegateDAO;

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

	public void remove(Serializable vo) {
		delegateDAO.remove(vo);
	}

	public void update(Serializable vo) {
		delegateDAO.update(vo);
	}

	public Collection execute(String sql, Collection<Parameter> params, int type) {
		return delegateDAO.execute(sql, params, type);
	}

	public Collection execute(String sql, Map<String, ?> map, int type) {
		return delegateDAO.execute(sql, map, type);
	}

	public DataPackage executeQuery(String sql, DBQueryParam param) {
		return delegateDAO.executeQuery(sql, param);
	}

	public List query(String sql) {
		return delegateDAO.query(sql);
	}

	public List query(String sql, Collection<Parameter> params) {
		return delegateDAO.query(sql, params);
	}

	public List query(String sql, Map<String, ?> map) {
		return delegateDAO.query(sql, map);
	}

	public int update(String sql) {
		return delegateDAO.update(sql);
	}

	public int update(String sql, Collection<Parameter> params) {
		return delegateDAO.update(sql, params);
	}

	public int update(String sql, Map<String, ?> map) {
		return delegateDAO.update(sql, map);
	}

	public List queryNative(String sql) {
		return delegateDAO.queryNative(sql);
	}

	public List queryNative(String sql, Map<String, ?> map) {
		return delegateDAO.queryNative(sql, map);
	}

	public List queryNative(String sql, Class cls) {
		return delegateDAO.queryNative(sql, cls);
	}

	public List queryNative(String sql, Class cls, Map<String, ?> map) {
		return delegateDAO.queryNative(sql, cls, map);
	}

	public int updateNative(String sql) {
		return delegateDAO.updateNative(sql);
	}

	public int updateNative(String sql, Map<String, ?> map) {
		return delegateDAO.updateNative(sql, map);
	}

	public SessionManager getSessionManager() {
		return delegateDAO.getSessionManager();
	}

	public void setSessionManager(SessionManager sessionManager) {
		delegateDAO.setSessionManager(sessionManager);
	}

	public GeneralDAO getDelegateDAO() {
		return delegateDAO;
	}

	public void setDelegateDAO(GeneralDAO delegateDAO) {
		this.delegateDAO = delegateDAO;
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
	
	public DataPackage query(MultiTableQuery mtq, Integer queryType)
			throws Exception {
		return delegateDAO.query(mtq, queryType);
	}

	public DataPackage query(MultiTableQuery mtq) throws Exception {
		return delegateDAO.query(mtq);
	}
}
