package org.ljdp.common.ehcache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.ljdp.common.cache.CacheRegion;

/**
 * 使用Ehcache实现的缓存功能
 * @author hzy
 *
 */
public class EhCacheRegion implements CacheRegion {

	private Cache cache;
	private int type;
	
	public EhCacheRegion(Cache cache, int type) {
		this.cache = cache;
		this.type = type;
	}

	@Override
	public int putData(String key, Object data) {
		cache.put(new Element(key, data));
		return 0;
	}

	@Override
	public Object getData(String key) {
		Element el = cache.get(key);
		if(el != null) {
			return el.getObjectValue();
		}
		return null;
	}

	@Override
	public void removeData(String key) {
		cache.remove(key);
	}

	@Override
	public boolean containsKey(String key) {
		if(getData(key) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Set<String> keySet() {
		List<?> list = cache.getKeys();
		Set<String> sets = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			String k = (String) list.get(i);
			sets.add(k);
		}
		return sets;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Collection datas() {
		List<Object> datas = new ArrayList<Object>();
		List<?> list = cache.getKeys();
		for (int i = 0; i < list.size(); i++) {
			String k = (String) list.get(i);
			Object d = getData(k);
			if(d != null) {
				datas.add(d);
			}
		}
		return datas;
	}

	@Override
	public void clear(boolean force) {
		if(force) {
			cache.removeAll();
		}
	}

	@Override
	public boolean isAutoClear() {
		return true;
	}

	@Override
	public void setAutoClear(boolean auto) {
		throw new UnsupportedOperationException("ehcache下不需要此设置");
	}

	@Override
	public int getCacheType() {
		return type;
	}

	@Override
	public int getMaxStoreLimit() {
		return (int)cache.getCacheConfiguration().getMaxEntriesLocalHeap();
	}

	@Override
	public void setMaxStoreLimit(int maxStoreLimit) {
		cache.getCacheConfiguration().setMaxEntriesLocalHeap(maxStoreLimit);
	}

}
