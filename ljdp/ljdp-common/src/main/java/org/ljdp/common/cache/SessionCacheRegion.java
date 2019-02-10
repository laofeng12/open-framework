package org.ljdp.common.cache;

/**
 * 带有会话周期方式的数据缓存表
 * 也就是说：当数据超过survivalSecond时间没有使用，则数据将被丢弃。
 * 当数据量超出最大限制时，丢弃最老的数据，并保证存活时间小于baseSurvivalSecond的数据不会被丢弃
 * @author hzy
 *
 */
public class SessionCacheRegion extends LifeCacheRegion {

	@Override
	protected void onAccessData(TimelimitData cd) {
		cd.updateAccessedTime();
	}

	@Override
	public int getCacheType() {
		return CacheType.SESSION;
	}

}
