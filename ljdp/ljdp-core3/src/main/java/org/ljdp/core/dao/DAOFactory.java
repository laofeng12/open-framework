package org.ljdp.core.dao;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.core.db.session.SessionFactoryRouter;
import org.ljdp.core.db.session.SessionManager;
import org.ljdp.util.SimpleBeanContext;

public class DAOFactory {
	
	/**
	 * 获取基于指定实体对象操作和指定DAO对象的DAO
	 * 采用Spring bean容器的方式构造到实例，以便为dao增加切面功能
	 * @param daoClass DAO的定义
	 * @param entityClass 实体对象定义
	 * @param db 使用的数据库名
	 * @param multiDB 是否多数据库模式
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static DAO build(Class daoClass, Class entityClass, String db, boolean multiDB) {
		SpringContextManager.registerBean(daoClass.getName(), daoClass);
		DAO dao = (DAO)SpringContextManager.getBean(daoClass.getName());
		SessionFactoryRouter router;
		if(SpringContextManager.containsBean("db.SessionFactoryRouter")) {
			router = (SessionFactoryRouter)SpringContextManager.getBean("db.SessionFactoryRouter");
		} else {
			String sfr = SessionFactoryRouter.class.getName();
			String h3router = sfr.substring(0, sfr.lastIndexOf("."));
			h3router += ".hibernate3.Hibernate3SessionFactoryRouter";
			router = (SessionFactoryRouter)SimpleBeanContext.getSingleBean(h3router);
		}
		if(entityClass != null) {
			dao.setEntityClass(entityClass);
		}
		SessionManager sessionManager;
		if(StringUtils.isNotEmpty(db) && multiDB) {
			sessionManager = router.getSessionManager(db);
		} else if(entityClass != null && multiDB) {
			sessionManager = router.getSessionManager(entityClass);
		} else {
			sessionManager = router.getSessionManager();
		}
		DAO delegateDAO = sessionManager.newDAO();
		((AbstractDAO)dao).setDelegateDAO(delegateDAO);
		return dao;
	}
	
	/**
	 * 获取多表操作和自定义SQL执行的通用DAO
	 * @param db 使用的数据库名
	 * @param multiDB 是否多数据库模式
	 * @return
	 */
	protected static GeneralDAO build(String db, boolean multiDB) {
		SessionFactoryRouter router;
		if(SpringContextManager.containsBean("db.SessionFactoryRouter")) {
			router = (SessionFactoryRouter)SpringContextManager.getBean("db.SessionFactoryRouter");
		} else {
			String sfr = SessionFactoryRouter.class.getName();
			String h3router = sfr.substring(0, sfr.lastIndexOf("."));
			h3router += ".hibernate3.Hibernate3SessionFactoryRouter";
			router = (SessionFactoryRouter)SimpleBeanContext.getSingleBean(h3router);
		}
		SessionManager sessionManager;
		if(StringUtils.isNotEmpty(db) && multiDB) {
			sessionManager = router.getSessionManager(db);
		} else {
			sessionManager = router.getSessionManager();
		}
		SimpleDAO dao = new SimpleDAO();
		GeneralDAO delegateDAO = sessionManager.newGeneralDAO();
		dao.setDelegateDAO(delegateDAO);
		return dao;
	}
	
	/**
	 * 获取多表操作和自定义SQL执行的通用DAO
	 * 多数据库模式
	 * @param db 使用的数据库名
	 * @return
	 */
	public static GeneralDAO build(String db) {
		return build(db, true);
	}
	
	/**
	 * 获取多表操作和自定义SQL执行的通用DAO
	 * @param db 使用的数据库名
	 * @return
	 */
	public static GeneralDAO build() {
		return build(null, false);
	}
	
	/**
	 * 获取基于指定实体对象操作和指定DAO对象的DAO
	 * @param daoClass DAO的定义
	 * @param entityClass 实体对象定义
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> DAO<T> build(Class daoClass, Class<T> entityClass) {
		return build(daoClass, entityClass, null, false);
	}
	
	/**
	 * 获取基于指定DAO对象的DAO。（指定的DAO对象已经定义了实体对象）
	 * 多数据库模式
	 * @param daoClass
	 * @param db
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static DAO build(Class daoClass, String db) {
		return build(daoClass, null, db, true);
	}

	/**
	 * 获取基于指定DAO对象的DAO。（指定的DAO对象已经定义了实体对象）
	 * @param daoClass
	 * @param db
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static DAO build(Class daoClass) {
		return build(daoClass, null, null, false);
	}
	
	/**
	 * 获取基于指定实体对象操作的通用型DAO
	 * 多数据库模式
	 * @param entityClass 实体对象
	 * @param db
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> CommonDAO<T> buildCommon(Class<T> entityClass, String db) {
		return (CommonDAO)build(CommonDAO.class, entityClass, db, true);
	}
	
	/**
	 * 获取基于指定实体对象操作的通用型DAO
	 * @param entityClass 实体对象
	 * @param db
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> CommonDAO<T> buildCommon(Class<T> entityClass) {
		return (CommonDAO)build(CommonDAO.class, entityClass, null, false);
	}
	
}

