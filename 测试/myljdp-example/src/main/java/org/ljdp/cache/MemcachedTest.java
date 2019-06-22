package org.ljdp.cache;

import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedTest {

	public static void main(String[] args) {
		ICacheManager<MemcachedClient> manager;
		manager = CacheUtil.getCacheManager();
		manager.start();
		
		try {
			MemcachedClient sc = manager.getCache("sessionCache");
			sc.add("test", 1000, "你好，我是测试");
			
			System.out.println(sc.get("test")+"");
			
			List<SomeData> list = new ArrayList<>();
			list.add(new SomeData("张三1"));
			list.add(new SomeData("李四1"));
			MemcachedClient cc = manager.getCache("commonCache");
			cc.add("datalist", 1000, list);
			
			System.out.println(cc.get("datalist").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		manager.stop();
	}

}
