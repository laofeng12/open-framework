package org.ljdp.core.service;

import org.ljdp.common.config.Constant;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.namespace.NameNotFoundException;
import org.ljdp.component.namespace.NameSpaceManager;
import org.ljdp.core.dxproxy.TransactionWrapper;

public class ServiceFactory {
	private static NameSpaceManager spNameSpace;

	/**
	 * 构建由服务提供者的Class获得服务提供者的实现类,通过Spring容器的自动代理增加事务等功能.
	 * 
	 * @param spClass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object buildByProviderClass(Class spClass) {
		Object sp;
		if (Env.current().transactionManager()
				.equals(Constant.TransactionManager.SPRING)) {
			boolean isSingleton = true;
			if (spClass.equals(CommonSP.class)
					|| spClass.equals(GeneralSP.class)) {
				isSingleton = false;
			}
			String name = spClass.getName();
			if (isSingleton) {
				name += "_s";
			}
			SpringContextManager.registerBean(name, spClass, isSingleton);
			sp = SpringContextManager.getBean(name);
		} else if (Env.current().transactionManager()
				.equals(Constant.TransactionManager.JDBC)) {
			try {
				sp = TransactionWrapper.decorate(spClass.newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				sp = spClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sp;
	}

	/**
	 * 构建由参数所指的服务接口获取实现此接口的服务提供者,使用默认的SP查找器.
	 * 
	 * @param servIClass
	 *            服务接口
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static Object build(Class servIClass) throws ClassNotFoundException,
			NameNotFoundException {
		return build(servIClass, getSpNameSpace());
	}

	/**
	 * 构建由参数所指的服务接口获取实现此接口的服务提供者,用户需要提供一个SP查找器.
	 * 
	 * @param servIClass
	 *            服务接口
	 * @param spFinder
	 *            服务提供者的查找器
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NameNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static Object build(Class servIClass, NameSpaceManager spFinder)
			throws ClassNotFoundException, NameNotFoundException {
		Class spClass = spFinder.classSeek(servIClass, "default");
		return buildByProviderClass(spClass);
	}

	/**
	 * 构建通用的数据库服务Bean,提供常用的数据库存取功能.
	 * 
	 * @param voClass
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> CommonService<T> buildCommon(Class<T> voClass)
			throws ClassNotFoundException, NameNotFoundException {
		CommonService<T> sp = (CommonService) build(CommonService.class);
		sp.setEntityClass(voClass);
		return sp;
	}

	public static GeneralService buildGeneral() throws ClassNotFoundException,
			NameNotFoundException {
		GeneralService gs = (GeneralService) build(GeneralService.class);
		return gs;
	}

	public static NameSpaceManager getSpNameSpace() {
		if (spNameSpace == null) {
			if (SpringContextManager
					.containsBean("system.serviceprovide.NameSpaceManager")) {
				spNameSpace = (NameSpaceManager) SpringContextManager
						.getBean("system.serviceprovide.NameSpaceManager");
			} else {
				spNameSpace = new ServiceProvideNameSpace();
			}
		}
		return spNameSpace;
	}

}
