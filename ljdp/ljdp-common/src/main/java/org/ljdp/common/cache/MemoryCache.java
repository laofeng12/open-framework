package org.ljdp.common.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;

/**
 * 全局内存缓存管理
 * @author hzy
 * @deprecated JDK6开始由org.ljdp.common.ehcache.MemoryCache代替
 */
@Deprecated
public class MemoryCache {
	private static final String LIFE_COMMON = "cache.global.life.common";

	private static final Lock lock = new ReentrantLock();
	
	static {
		cache = Collections.synchronizedMap(new HashMap<String, CacheRegion>());
		initCache(LIFE_COMMON, CacheType.LIFE);
		config(LIFE_COMMON, CacheParam.MAX_STORE_LIMIT, new Integer(10000));
		
		ConfigFile cfg = Env.current().getConfigFile();
		int min = Integer.parseInt(cfg.getValue("cache.clear.interval", "30"));
		ScheduledExecutorService scheduleServer = Executors.newScheduledThreadPool(1);
		ClearThread clearThread = new ClearThread();
		long delay = min * 60;
		scheduleServer.scheduleWithFixedDelay(clearThread, delay, delay, TimeUnit.SECONDS);
	}
	
	/**
	 * 全局缓存
	 */
	private static Map<String, CacheRegion> cache;
	
	/**
	 * 初始化一类型缓存
	 * @param id 缓存标识，任意值
	 * @param type 缓存类型，参见：CacheType
	 */
	public static void initCache(String id, int type) {
		if(id == null || id.length() == 0) {
			throw new RuntimeException("GlobalCache.initCache key is null");
		}
		if(cache == null) {
			throw new RuntimeException("GlobalCache.initCache cache is null");
		}
		if( cache.containsKey(id) ) {
			return;
		}
		if(CacheType.LIFE == type) {
			CacheRegion pm = new LifeCacheRegion();
			cache.put(id, pm);
		} else if(CacheType.PERMANENT == type) {
			CacheRegion pm = new PerCacheRegion();
			cache.put(id, pm);
		} else if(CacheType.SESSION == type) {
			CacheRegion pm = new SessionCacheRegion();
			cache.put(id, pm);
		}
	}
	
	/**
	 * 设置一缓存参数
	 * @param id 缓存标识
	 * @param paramName 参数名称 参见：CacheParam
	 * @param paramValue 参数值
	 */
	public static void config(String id, String paramName, Object paramValue) {
		CacheRegion cr = getCache(id);
		if(paramName.equals(CacheParam.TIME_LIFE_MINUTE)) {
			LifeCacheRegion lcr = (LifeCacheRegion)cr;
			int survivalMinute = ((Integer)paramValue).intValue();
			lcr.setSurvivalSecond(survivalMinute * 60);
		}
		if(paramName.equals(CacheParam.AUTO_CLEAR)) {
			Boolean flag = (Boolean)paramValue;
			cr.setAutoClear(flag.booleanValue());
		}
		if(paramName.equals(CacheParam.MAX_STORE_LIMIT)) {
			int max = ((Integer)paramValue).intValue();
			cr.setMaxStoreLimit(max);
		}
		if(paramName.equals(CacheParam.TIME_MINLIFE_MINUTE)) {
			LifeCacheRegion lcr = (LifeCacheRegion)cr;
			int baseSurvival = ((Integer)paramValue).intValue();
			lcr.setBaseSurvivalSecond(baseSurvival * 60);
		}
	}
	
	/**
	 * 根据ID获取一缓存区
	 * @param id
	 * @return
	 */
	public static CacheRegion getCache(String id) {
		if(cache != null) {
			return cache.get(id);
		}
		return null;
	}
	
	public static Object getData(String id, String key) {
		return getCache(id).getData(key);
	}
	
	public static boolean containData(String id, String key) {
		return getCache(id).containsKey(key);
	}
	
	public static int putData(String id, String key, Object data) {
		return getCache(id).putData(key, data);
	}
	
	public static void removeData(String id, String key) {
		getCache(id).removeData(key);
	}
	
	/**
	 * 把数据存放进一个公用的具有生命时间的缓存区中
	 * @param key 数据标识
	 * @param data 数据
	 * @param mySurvivalTime 数据生存时间（秒）
	 */
	public static int putLifeData(String key, Object data, long survivalSecond) {
		LifeCacheRegion cr = (LifeCacheRegion)cache.get(LIFE_COMMON);
		return cr.putData(key, data, 0, survivalSecond);
	}
	
	/**
	 * 把数据存放进一个公用的具有生命时间的缓存区中
	 * @param key 数据标识
	 * @param data 数据
	 */
	public static int putLifeData(String key, Object data) {
		CacheRegion cr = cache.get(LIFE_COMMON);
		return cr.putData(key, data);
	}
	
	/**
	 * 从一个公用的具有生命时间的缓存区中取数据
	 * @param key 数据标识
	 * @return 数据
	 */
	public static Object getLifeData(String key) {
		CacheRegion cr = cache.get(LIFE_COMMON);
		return cr.getData(key);
	}
	
	public static void removeLifeData(String key) {
		CacheRegion cr = cache.get(LIFE_COMMON);
		cr.removeData(key);
	}
	
	/**
	 * 清除指定ID的缓存区，只清理允许并认为不在使用的数据。
	 * @param id
	 */
	public static void clearUseless(String id) {
		CacheRegion cr = cache.get(id);
		if(cr.isAutoClear()) {
			cr.clear(false);
		}
	}

	/**
	 * 清除所有不在使用的缓存。
	 */
	public static void clearUseless() {
		Iterator<String> iter = cache.keySet().iterator();
		while(iter.hasNext()) {
			String id = iter.next();
			clearUseless(id);
		}
	}
	
	protected static Collection<CacheRegion> getCacheRegions() {
		return cache.values();
	}
	
	public static void lock() {
		lock.lock();
	}
	
	public static void unlock() {
		lock.unlock();
	}
}

class ClearThread implements Runnable{

	public void run() {
		try {
			MemoryCache.lock();
			Iterator<CacheRegion> it = MemoryCache.getCacheRegions().iterator();
			while (it.hasNext()) {
				CacheRegion cr = (CacheRegion) it.next();
				if(cr.getCacheType() == CacheType.LIFE) {
					cr.clear(false);
				}
				if(cr.getCacheType() == CacheType.SESSION) {
					cr.clear(false);
				}
			}
		} finally {
			MemoryCache.unlock();
		}
	}
	
}
