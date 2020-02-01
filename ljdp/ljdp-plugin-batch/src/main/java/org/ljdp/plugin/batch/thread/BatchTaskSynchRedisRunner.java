package org.ljdp.plugin.batch.thread;

import java.time.Duration;
import java.util.List;

import org.ljdp.common.spring.SpringContext;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.task.BaseBatchTask;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 定时把批量任务池里的任务存放到redis，用于支撑在分布式架构下获取任务信息
 * @author hzy0769
 *
 */
public class BatchTaskSynchRedisRunner implements Runnable {

	public static boolean running = true;
	public static int Interval = 1500;//触发间隔
	
	@Override
	public void run() {
		System.out.println("[BatchTaskSynchRedisRunner]启动日志批量保存线程...");
		RedisTemplate<String, Object> redisTemplate;
		try {
			Thread.sleep(15000);
			while(true) {
				if(SpringContext.mySpringContext != null) {
					redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
					if(redisTemplate != null) {
						System.out.println("[BatchTaskSynchRedisRunner]初始化完成");
						break;
					}
				}
				System.out.println("[BatchTaskSynchRedisRunner]LJDP还未初始化完成，继续等待...");
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR][BatchTaskSynchRedisRunner]初始化失败!!!!!!!!");
			return;
		}
		while(running) {
			try {
				Thread.sleep(Interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<BaseBatchTask> alltasks = TaskPoolManager.getFgPool().getAllTask();
			for(int i=0; i < alltasks.size(); i++) {
				BaseBatchTask task = alltasks.get(i);
				redisTemplate.opsForValue().set(task.getId(), task, Duration.ofMinutes(60));
			}
		}
	}

}
