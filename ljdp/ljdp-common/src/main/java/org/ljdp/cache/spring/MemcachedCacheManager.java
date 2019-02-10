package org.ljdp.cache.spring;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

public class MemcachedCacheManager extends AbstractCacheManager{

	private Collection<? extends MemcachedCache> caches; 
	
	/** 
	   * Specify the collection of Cache instances to use for this CacheManager. 
	   */ 
	public void setCaches(Collection<? extends MemcachedCache> caches) { 
		this.caches = caches; 
	} 
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		return this.caches;
	}

}
