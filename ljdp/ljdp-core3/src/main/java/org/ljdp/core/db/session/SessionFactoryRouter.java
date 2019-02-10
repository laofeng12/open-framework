package org.ljdp.core.db.session;

/**
 * 多数据库转发路由器接口
 * @author Hzy
 *
 */
public interface SessionFactoryRouter {
    public final static String DB_DEFAULT = "DEFAULT";

	public SessionManager getSessionManager();
	
	@SuppressWarnings("rawtypes")
	public SessionManager getSessionManager(Class voClass);
	
	public SessionManager getSessionManager(String db);

	/**
	 * 获取默认使用的数据库
	 * @return
	 */
	public String getDefaultDB();
}
