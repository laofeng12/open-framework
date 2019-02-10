package org.ljdp.core.dxproxy;

import java.lang.reflect.Proxy;

public final class TransactionWrapper {

	/**
	 * 装饰原始的业务代表对象，返回一个与业务代表对象有相同接口的代理对象
	 */
	public static Object decorate(Object server) {
		return Proxy.newProxyInstance(server.getClass().getClassLoader(), 
				server.getClass().getInterfaces(), 
				new XAWrapperHandler(server));
	}
}
