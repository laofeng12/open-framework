package org.ljdp.common.cache;

import java.util.Collection;
import java.util.Set;

/**
 * 内存缓存区域
 * @author hzy
 *
 */
public interface CacheRegion {

	/**
	 * 把数据存进缓存区
	 * @param key
	 * @param data
	 */
	public int putData(String key, Object data);

	/**
	 * 从缓存区取数据
	 * @param key
	 * @return
	 */
	public Object getData(String key);

	public void removeData(String key);

	public boolean containsKey(String key);

	public Set<String> keySet();

	@SuppressWarnings("rawtypes")
	public Collection datas();

	/**
	 * true:强制清理所有数据，false：只清理（不在使用/过期/允许清理）的数据
	 * @param force
	 */
	public void clear(boolean force);
	
	public boolean isAutoClear();
	
	public void setAutoClear(boolean auto);
	
	public int getCacheType();
	
	/**
	 * 最大存放数据的数量，当达超出最大数时，不同缓存类型有不同的处理方式
	 * @return
	 */
	public int getMaxStoreLimit();
	
	/**
	 * 等于0时表示没有限制
	 * @param maxStoreLimit
	 */
	public void setMaxStoreLimit(int maxStoreLimit);

}