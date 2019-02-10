package org.ljdp.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Spring 上下文管理器，ContextManager
 * @author Hzy
 *
 */
public class SpringContextManager {
	public static final Logger log = LoggerFactory.getLogger(SpringContextManager.class);
	public static SpringContext defaultContext = SpringContext.getInstance();
	
	public static ApplicationContext getContext() {
		return defaultContext.getContext();
	}
	
	public static DefaultListableBeanFactory getBeanFactory() {
		AbstractApplicationContext context = (AbstractApplicationContext)getContext();
		return (DefaultListableBeanFactory)context.getBeanFactory();
	}

	public static boolean containsBean(String beanName) {
		return getContext().containsBean(beanName);
	}
	
	public static Object getBean(String name) {
		return getContext().getBean(name);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return getContext().getBean(requiredType);
	}
	
	/**
	 * 注册bean定义,使用protype
	 * @param idOrName
	 * @param beanClass
	 */
	@SuppressWarnings("rawtypes")
	public static void registerBean(String idOrName,Class beanClass) {
		registerBean(idOrName, beanClass, false);
	}
	
	/**
	 * 注册bean定义
	 * @param idOrName
	 * @param beanClass
	 * @param isSingleton
	 */
	@SuppressWarnings("rawtypes")
	public static void registerBean(String idOrName,Class beanClass,boolean isSingleton) {
		DefaultListableBeanFactory beanFactory = getBeanFactory();
		if(!beanFactory.containsBean( idOrName )){
			
			RootBeanDefinition beanDefinition = new RootBeanDefinition();
			beanDefinition.setBeanClassName(beanClass.getName());
			if(isSingleton) {
				beanDefinition.setScope("singleton");
			} else {
				beanDefinition.setScope("prototype");
			}
			beanFactory.registerBeanDefinition( idOrName , beanDefinition);
			
			if(log.isDebugEnabled()) {
				log.debug("Register Bean: " + idOrName +" ?singleton=" + isSingleton);
			}
		}
	}
	
	public static void registerShutdownHook() {
		AbstractApplicationContext ctx = (AbstractApplicationContext)getContext();
		ctx.registerShutdownHook();
	}
	
}
