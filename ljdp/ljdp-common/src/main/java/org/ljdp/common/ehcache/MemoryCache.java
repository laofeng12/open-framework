package org.ljdp.common.ehcache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheRegion;
import org.ljdp.common.cache.CacheType;

public class MemoryCache {
	public final static int DEFAULT_MAX_STORE = 10000;//默认最大存放数量
	
	public final static Map<String,Integer> TYPEMAP = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	public static void initCache(String id, int type) {
		if(id == null || id.length() == 0) {
			throw new RuntimeException("GlobalCache.initCache key is null");
		}
		CacheManager manager = CacheManager.getInstance();
		if(manager.getCache(id) != null) {
			return;
		}
		
		CacheConfiguration cfg;
		if(CacheType.LIFE == type) {
			cfg = new CacheConfiguration(id, DEFAULT_MAX_STORE)
				.eternal(false)
				.timeToLiveSeconds(60*60)//默认存活1小时
				.timeToIdleSeconds(0);
		} else if(CacheType.PERMANENT == type) {
			cfg = new CacheConfiguration(id, DEFAULT_MAX_STORE)
				.eternal(false)
				.timeToLiveSeconds(0)
				.timeToIdleSeconds(0);
			
		} else if(CacheType.SESSION == type) {
			cfg = new CacheConfiguration(id, DEFAULT_MAX_STORE)
				.eternal(false)
				.timeToLiveSeconds(0)
				.timeToIdleSeconds(60*60);//默认空闲1小时
		} else {
			throw new RuntimeException("type is null");
		}
		Cache c = new Cache(cfg);
		manager.addCache(c);
		TYPEMAP.put(id, type);
	}
	
	public static void config(String id, String paramName, Object paramValue) {
		CacheManager manager = CacheManager.getInstance();
		Cache c = manager.getCache(id);
		final int type = TYPEMAP.get(id);
		if(paramName.equals(CacheParam.TIME_LIFE_MINUTE)) {
			int survivalMinute = ((Integer)paramValue).intValue();
			if(CacheType.LIFE == type) {
				c.getCacheConfiguration().setTimeToLiveSeconds(survivalMinute*60);
			} else if(CacheType.SESSION == type) {
				c.getCacheConfiguration().setTimeToIdleSeconds(survivalMinute*60);
			}
		}
		if(paramName.equals(CacheParam.MAX_STORE_LIMIT)) {
			int max = ((Integer)paramValue).intValue();
			c.getCacheConfiguration().setMaxEntriesLocalHeap(max);
		}
	}
	
	public static CacheRegion getCache(String id) {
		Cache c = getEhCache(id);
		if(c == null) {
			return null;
		}
		EhCacheRegion cr = new EhCacheRegion(c, TYPEMAP.get(id));
		return cr;
	}
	
	public static Cache getEhCache(String id) {
		return CacheManager.getInstance().getCache(id);
	}
	
	public static Object getData(String id, String key) {
		Element el = getEhCache(id).get(key);
		if(el != null) {
			return el.getObjectValue();
		}
		return el;
	}

	public static boolean containData(String id, String key) {
		if(getData(id, key) != null) {
			return true;
		}
		return false;
	}
	
	public static void putData(String id, String key, Object data) {
		getEhCache(id).put(new Element(key, data));
	}
	
	public static void removeData(String id, String key) {
		getEhCache(id).remove(key);
	}
	
	public static void clearAll() {
		CacheManager.getInstance().clearAll();
	}

}
