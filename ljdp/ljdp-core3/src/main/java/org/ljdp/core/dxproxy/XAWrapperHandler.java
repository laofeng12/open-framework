package org.ljdp.core.dxproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.ljdp.core.db.session.SessionUtil;

/**
 * 一个简单的事务动态代理,只支持单数据库模式
 * @author Hzy
 *
 */
public class XAWrapperHandler implements InvocationHandler {
	Object server;

	public XAWrapperHandler() {
		
	}
	
	public XAWrapperHandler(Object server) {
		this.server = server;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
		Object retVal;
		if(method.getName().startsWith("do")) {			
			try {
				SessionUtil.openSession();
				SessionUtil.beginTransaction();
				retVal = method.invoke(server, args);
				SessionUtil.commitTransaction();
			} catch (InvocationTargetException ite) {
				SessionUtil.rollbackTransaction();
				throw ite.getTargetException();
			} catch (Throwable e) {
				SessionUtil.rollbackTransaction();
				throw e;
			} finally {
				SessionUtil.closeSession();
			}
		} else {
			SessionUtil.openSession();
			retVal = method.invoke(server, args);
		}
        return retVal;
	}
	
}
