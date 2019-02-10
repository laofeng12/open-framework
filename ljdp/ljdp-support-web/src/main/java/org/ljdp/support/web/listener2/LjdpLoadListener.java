package org.ljdp.support.web.listener2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.ljdp.common.config.Env;
import org.ljdp.support.web.loader.LjdpWebLoader;

@WebListener
public class LjdpLoadListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LjdpWebLoader.contextDestroyed(arg0);
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Env.buildCurrentEnv();
	}

}
