package org.ljdp.cache.spring;

import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.ljdp.cache.memcached.MemcachedClientConfig;
import org.springframework.cache.Cache; 
import org.springframework.cache.support.SimpleValueWrapper;

import net.rubyeye.xmemcached.MemcachedClient; 

public class MemcachedCache implements Cache {
	private String name;
	private MemcachedClient mclient;
	private int expTime = 0;
	
	private void initClient() {
		if(mclient == null) {
			if(name == null) {
				System.out.println("[MemcachedCache][error]未配置缓存名");
				return;
			}
			ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
			mclient = manager.getCache(name);
			MemcachedClientConfig cc = manager.getClientConfig(name);
			expTime = cc.getDefaultExpire();
		}
	}
	
	@Override
	public void clear() {
		
	}

	@Override
	public void evict(Object key) {
		try {
			initClient();
			mclient.deleteWithNoReply(key.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ValueWrapper get(Object key) {
		if(key != null) {
			try {
				initClient();
				Object val = mclient.get(key.toString());
				if(val != null) {
					ValueWrapper result = new SimpleValueWrapper(val);
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public <T> T get(Object key, Class<T> clz) {
		if(key != null) {
			try {
				initClient();
				Object val = mclient.get(key.toString());
				return (T)val;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}
	public void setName(String name) { 
		this.name = name; 
	}

	@Override
	public Object getNativeCache() {
		return mclient;
	}

	@Override
	public void put(Object key, Object value) {
		if(key == null) {
			return;
		}
		initClient();
		try {
			mclient.add(key.toString(), expTime, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		if(key == null) {
			return null;
		}
		initClient();
		try {
			Object val = mclient.get(key.toString());
			if(val != null) {
				return new SimpleValueWrapper(val);
			}
			mclient.add(key.toString(), 0, value, 5L);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SimpleValueWrapper(value);
	}

}
