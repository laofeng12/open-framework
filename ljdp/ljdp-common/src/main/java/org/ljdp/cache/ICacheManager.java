package org.ljdp.cache;

import org.ljdp.cache.memcached.MemcachedClientConfig;

public interface ICacheManager<T> {
	public abstract T getCache(String paramString);

	public abstract void setConfigFile(String paramString);

	public abstract void start();

	public abstract void stop();

	public MemcachedClientConfig getClientConfig(String name);
}
