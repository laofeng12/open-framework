package org.ljdp.common.spring;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.Env;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringContext implements ApplicationContextAware{
	public static SpringContext mySpringContext = null;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	public ApplicationContext context;
	
	private SpringContext() {
		if(mySpringContext == null) {
			mySpringContext = this;
			log.info("LJDP初始化...[嵌入中间件服务器]");
			Env.buildCurrentEnv();
		}
	}
	
	private SpringContext(String resource) {
		init(resource);
		Env.buildCurrentEnv();
	}
	
	public static SpringContext getInstance() {
		if(null == mySpringContext) {
			mySpringContext = new SpringContext(Env.current().getSpringCfg());
		}
		return mySpringContext;
	}
	
	/**
	 * spring boot使用
	 * @return
	 */
	public static SpringContext getEmbedInstance() {
		return new SpringContext();
	}
	
	public void init() {
		init(Env.current().getSpringCfg());
	}
	
	public void init(String resource) {
		log.info("LJDP初始化...[独立运行:"+resource+"]");
		if(null == context && StringUtils.isNotBlank(resource)) {
			try {
				context = new ClassPathXmlApplicationContext(resource);
			} catch (Exception e) {
				try {
					context = new FileSystemXmlApplicationContext(resource);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public ApplicationContext getContext() {
		if(context == null) {
			init();
		}
		return context;
	}
	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
	}
	
}
