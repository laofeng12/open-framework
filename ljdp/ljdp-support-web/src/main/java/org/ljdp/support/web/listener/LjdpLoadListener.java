package org.ljdp.support.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheType;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.support.log.ApiLogSaveRunner;

import net.rubyeye.xmemcached.MemcachedClient;
import net.sf.ehcache.CacheManager;

@WebListener
public class LjdpLoadListener implements ServletContextListener {
	
	private boolean loadMemcached = false;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		ApiLogSaveRunner.running = false;
		TaskPoolManager.getFgPool().shutdownAll();
		TaskPoolManager.getBsPool().shutdownAll();
		MemoryCache.clearAll();
		CacheManager.getInstance().shutdown();
		if(loadMemcached) {
			CacheUtil.getCacheManager().stop();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Env.buildCurrentEnv();
		System.out.println("[LjdpLoadListener]java.io.tmpdir="+System.getProperty("java.io.tmpdir"));
		System.out.println("[LjdpLoadListener]user.home="+System.getProperty("user.home"));
		System.out.println("[LjdpLoadListener]user.dir="+System.getProperty("user.dir"));
		try {
			ConfigFile mycfg = Env.getCurrent().getConfigFile();
	    	//附件上传临时保存区
	    	MemoryCache.initCache(DictConstants.CACHE_ATTACH, CacheType.LIFE);
	    	MemoryCache.config(DictConstants.CACHE_ATTACH, CacheParam.TIME_LIFE_MINUTE, 30);
	    	
	    	//短信验证码缓存
	    	MemoryCache.initCache(DictConstants.CACHE_SMSCODE, CacheType.LIFE);
	    	MemoryCache.config(DictConstants.CACHE_SMSCODE, CacheParam.TIME_LIFE_MINUTE, 10);
	    	
	    	String sessionCache = mycfg.getValue("session.cache");
	    	System.out.println("[LjdpLoadListener]session.cache="+sessionCache);
	    	
	    	//登录session缓存
	    	String loginTimeoutCfg = mycfg.getValue("session.login.timeout.min");
	    	System.out.println("[LjdpLoadListener]session.login.timeout.min="+loginTimeoutCfg);
	    	int loginTimeout = 120;
	    	if(StringUtils.isNotEmpty(loginTimeoutCfg)) {
	    		try {
	    			loginTimeout = Integer.parseInt(loginTimeoutCfg);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	MemoryCache.initCache(DictConstants.CACHE_SESSION, CacheType.LIFE);
	    	MemoryCache.config(DictConstants.CACHE_SESSION, CacheParam.TIME_LIFE_MINUTE, loginTimeout);
	    	
	    	String logentity = mycfg.getValue("request.log.entity");
	    	if(StringUtils.isNotEmpty(logentity)) {
	    		System.out.println("[LjdpLoadListener]request.log.entity="+logentity);
	    		new Thread(new ApiLogSaveRunner()).start();
	    	}
	    	String memcached = mycfg.getValue("cache.memcached");
	    	if(StringUtils.isNotEmpty(memcached)) {
	    		ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
	    		manager.setConfigFile(memcached);
	    		manager.start();
	    		loadMemcached = true;
	    		System.out.println("[LjdpLoadListener]cache.memcached="+memcached);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
