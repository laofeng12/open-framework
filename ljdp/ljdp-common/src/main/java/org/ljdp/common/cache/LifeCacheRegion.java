package org.ljdp.common.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 带生命期的数据表。
 * 数据在第一次载入表时就设置了一个不变的生存时间(survivalSecond秒)，当到达生存时间后数据将失效。
 * 当数据量超出最大限制时，丢弃最老的数据，并保证存活时间小于最小生存时间(baseSurvivalSecond)的数据不会被丢弃
 * @author hzy
 *
 */
public class LifeCacheRegion implements CacheRegion {
	public long survivalSecond = 60 * 60L;//数据保存时间，单位：秒 （默认一小时）
	private long baseSurvivalSecond = 0;//基本生存时间，在此时间内不会被回收，0表示不起作用
	
	private int maxStoreLimit = 0;
	
	private Map<String, TimelimitData> cache = Collections.synchronizedMap(new HashMap<String, TimelimitData>());
	
	public synchronized int putData(String key, Object data, long minSurivalSecond, long maxSurivalSecond) {
		if(maxStoreLimit > 0 && cache.size() >= maxStoreLimit) {
			//丢弃1/10最老的数据
			List<TimelimitData> list = new LinkedList<TimelimitData>();
			TimelimitData[] items = cache.values().toArray(new TimelimitData[] {});
			for (int i = 0; i < items.length; i++) {
				TimelimitData td = items[i];
				list.add(td);
			}
			Collections.sort(list);
			int clearSize = maxStoreLimit / 10;
			for (int i = 0; i < clearSize; i++) {
				TimelimitData d = list.get(i);
				if(!d.isGrowUp()) {
					break;
				}
				removeData(d.getKey());
			}
		}
		if(maxStoreLimit > 0 && cache.size() >= maxStoreLimit) {
			System.out.println("缓存池已达到最大值，限制="+maxStoreLimit);
			return CacheResult.AMOUNT_EXCEED;
		}
		TimelimitData cd = new TimelimitData(data, maxSurivalSecond);
		cd.setKey(key);
		if(minSurivalSecond > 0) {
			cd.setBaseSurvivalSecond(minSurivalSecond);
		}
		cache.put(key, cd);
		return CacheResult.SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see org.opensource.ljdp.common.cache.CacheRegion#putData(java.lang.String, java.lang.Object)
	 */
	public int putData(String key, Object data) {
		return putData(key, data, baseSurvivalSecond, survivalSecond);
	}
	
	/** 
	 * 从缓存区取数据，如果数据已经过时，则返回NULL
	 * @see org.ljdp.common.cache.CacheRegion#getData(java.lang.String)
	 */
	public Object getData(String key) {
		if(cache.containsKey(key)) {
			TimelimitData cd = (TimelimitData)cache.get(key);
			if(cd.isOverdue()) {
				removeData(key);
				return null;
			}
			onAccessData(cd);
			return cd.getData();
		}
		return null;
	}
	
	/**
	 * 访问数据时触发的事件
	 * @param cd
	 */
	protected void onAccessData(TimelimitData cd) {
		
	}
	
	/* (non-Javadoc)
	 * @see org.opensource.ljdp.common.cache.CacheRegion#removeData(java.lang.String)
	 */
	public synchronized void removeData(String key) {
		TimelimitData cd = (TimelimitData)cache.get(key);
		if(cd != null) {
			cd.release();
			cache.remove(key);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.opensource.ljdp.common.cache.CacheRegion#containsKey(java.lang.String)
	 */
	public boolean containsKey(String key) {
		boolean flag =  cache.containsKey(key);
		if(flag) {
			TimelimitData cd = (TimelimitData)cache.get(key);
			if(cd.isOverdue()) {
				removeData(key);
				flag = false;
			}
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see org.opensource.ljdp.common.cache.CacheRegion#keySet()
	 */
	public Set<String> keySet(){
		return cache.keySet();
	}
	
	/* (non-Javadoc)
	 * @see org.opensource.ljdp.common.cache.CacheRegion#datas()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized Collection datas() {
		ArrayList list = new ArrayList();
		String[] items = cache.keySet().toArray(new String[] {});
		for (int i = 0; i < items.length; i++) {
			String key = items[i];
			if(cache.containsKey(key)) {
				Object obj = getData(key);
				if(obj != null) {
					list.add(obj);
				}
			}
		}
		return list;
	}
	
	public synchronized void clear(boolean force) {
		if(force) {
			TimelimitData[] items = cache.values().toArray(new TimelimitData[] {});
			for (int i = 0; i < items.length; i++) {
				TimelimitData td = items[i];
				if(td != null) {
					td.release();
				}
			}
			cache.clear();
		} else {
			clearOverdue();
		}
	}
	
	public synchronized void clearOverdue() {
		String[] items = cache.keySet().toArray(new String[] {});
		for (int i = 0; i < items.length; i++) {
			String key = items[i];
			if(cache.containsKey(key)) {
				TimelimitData cd = (TimelimitData)cache.get(key);
				if(cd != null && cd.isOverdue()) {
					removeData(key);
				}
			}
		}
	}

	public long getSurvivalSecond() {
		return survivalSecond;
	}

	public void setSurvivalSecond(long survivalSecond) {
		this.survivalSecond = survivalSecond;
	}

	public boolean isAutoClear() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAutoClear(boolean auto) {
		// TODO Auto-generated method stub
		
	}

	public int getCacheType() {
		return CacheType.LIFE;
	}

	public int getMaxStoreLimit() {
		return maxStoreLimit;
	}

	public void setMaxStoreLimit(int maxStoreLimit) {
		this.maxStoreLimit = maxStoreLimit;
	}

	public long getBaseSurvivalSecond() {
		return baseSurvivalSecond;
	}

	public void setBaseSurvivalSecond(long baseSurvivalSecond) {
		this.baseSurvivalSecond = baseSurvivalSecond;
	}

}
