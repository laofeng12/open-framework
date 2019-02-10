package org.ljdp.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.db.MultiTableQuery;
import org.ljdp.core.db.session.SessionManager;
import org.ljdp.core.query.Parameter;

@SuppressWarnings("rawtypes")
public interface GeneralDAO {
	public void clear();

	public Serializable create(Serializable vo);

	public void remove(Serializable vo);
	
	public void update(Serializable vo);
	
	public Collection execute(String sql, Collection<Parameter> params, int type);
	public Collection execute(String sql, Map<String, ?> map, int type);
	public DataPackage executeQuery(String sql, DBQueryParam param);
	
	public List query(String sql);
	
	public List query(String sql, Collection<Parameter> params);
	public List query(String sql, Map<String, ?> map);
	
	public int update(String sql);
	
	public int update(String sql, Collection<Parameter> params);
	public int update(String sql, Map<String, ?> map);
	
	public List queryNative(String sql);
	public List queryNative(String sql, Map<String, ?> map);
	
	public List queryNative(String sql, Class cls);
	public List queryNative(String sql, Class cls, Map<String, ?> map);
	
	public int updateNative(String sql);
	public int updateNative(String sql, Map<String, ?> map);
	
	public DataPackage query(MultiTableQuery mtq, Integer queryType) throws Exception;
	public DataPackage query(MultiTableQuery mtq) throws Exception;

	public SessionManager getSessionManager();

	public void setSessionManager(SessionManager sessionManager);
	
	public void setPage(int firstResult, int maxResults);
	public void cancelPaging();
	
	public void flush();
	public void commit();
}
