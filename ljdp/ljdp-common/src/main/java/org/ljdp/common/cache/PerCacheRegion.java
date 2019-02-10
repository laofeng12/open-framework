package org.ljdp.common.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ljdp.component.workflow.Releaseable;

/**
 * 永久缓存区，不会自动释放
 * @author Administrator
 *
 */
public class PerCacheRegion implements CacheRegion {
	
	private Map<String, Object> cache = Collections.synchronizedMap(new HashMap<String, Object>());
	private int maxStoreLimit = 0;
	private boolean autoClear;
	
	public void clear(boolean force) {
		if(force) {
			cache.clear();
		}
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public Collection<Object> datas() {
		return cache.values();
	}

	public Object getData(String key) {
		return cache.get(key);
	}

	public Set<String> keySet() {
		return cache.keySet();
	}

	public int putData(String key, Object data) {
		if(maxStoreLimit > 0 && cache.size() >= maxStoreLimit) {
			System.out.println("PerCacheRegion Exceeds the maximum when puting ["+key+"], the limit is "+maxStoreLimit);
			return CacheResult.AMOUNT_EXCEED;
		}
		cache.put(key, data);
		return CacheResult.SUCCESS;
	}

	public void removeData(String key) {
		Object obj = cache.get(key);
		if(obj != null) {
			if(obj instanceof Releaseable) {
				Releaseable r = (Releaseable)obj;
				r.release();
			}
		}
		cache.remove(key);
	}
	
	public boolean isAutoClear() {
		return autoClear;
	}

	public void setAutoClear(boolean auto) {
		autoClear = auto;
	}
	
	public int getCacheType() {
		return CacheType.PERMANENT;
	}

	public int getMaxStoreLimit() {
		return maxStoreLimit;
	}

	public void setMaxStoreLimit(int maxStoreLimit) {
		this.maxStoreLimit = maxStoreLimit;
	}
}
