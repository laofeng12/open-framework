package org.ljdp.common.test;

import java.util.Collection;
import java.util.Iterator;

import org.ljdp.common.cache.CacheParam;
import org.ljdp.common.cache.CacheType;
import org.ljdp.common.ehcache.MemoryCache;

public class CacheTRun {

	public static void main(String[] args) {
		MemoryCache.initCache("test", CacheType.LIFE);
		MemoryCache.config("test", CacheParam.TIME_LIFE_MINUTE, 1);
		
		MemoryCache.putData("test", "a", new MyData("1"));
		MemoryCache.putData("test", "b", new MyData("2"));
		MemoryCache.putData("test", "c", new MyData("3"));
		MemoryCache.putData("test", "d", new MyData("4"));

		Collection list = MemoryCache.getCache("test").datas();
		
		try {
			Thread.sleep(70000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(MemoryCache.containData("test", "a"));
		System.out.println(MemoryCache.getData("test", "a"));
		System.out.println(MemoryCache.containData("test", "a"));
		
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object o = (Object) it.next();
			System.out.println(o);
		}
		
		System.out.println("============");
		list = MemoryCache.getCache("test").datas();
		it = list.iterator();
		while (it.hasNext()) {
			Object o = (Object) it.next();
			System.out.println(o);
		}
	}

}
