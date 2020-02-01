package org.openjava.boot;

import org.ljdp.common.spring.SpringContext;
import org.ljdp.plugin.batch.thread.BatchTaskSynchRedisRunner;
import org.ljdp.support.web.loader.LjdpWebLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class LjdpBootRunner implements ApplicationRunner {
	
	@Autowired
	private SpringContext springContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LjdpWebLoader.contextInitialized();
		//文件导入任务同步redis
		(new Thread(new BatchTaskSynchRedisRunner())).start();
		System.out.println("LJDP启动成功");
	}

}
