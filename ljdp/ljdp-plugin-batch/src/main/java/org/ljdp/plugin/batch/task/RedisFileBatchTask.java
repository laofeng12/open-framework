package org.ljdp.plugin.batch.task;

import java.time.Duration;

import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.strategy.FileBusinessObject;
import org.ljdp.module.filetask.BOFileBatchTask;
import org.ljdp.plugin.batch.model.BatchFileDic;
import org.ljdp.plugin.batch.pool.TaskPoolManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * LJDP架构封装的文件数据批量处理任务。
 * 实现了任务结束后释放占用的线程池，
 * 并把任务移至一个临时的内存池中保存(可选，根据endInMemoryTemp)，便于快速查询
 * 
 * @author hzy
 *
 */
public class RedisFileBatchTask extends BOFileBatchTask {
	private static final long serialVersionUID = 385027441905010674L;
	private boolean endInMemoryTemp = true;
	
	private transient RedisTemplate<String, Object> redisTemplate;

	public RedisFileBatchTask(FileBusinessObject bo) {
		super(bo);
		try {
			RedisTemplate redisTemplate = (RedisTemplate)SpringContextManager.getBean("redisTemplate");
			if(redisTemplate != null) {
				this.setRedisTemplate(redisTemplate);
			}
		} catch(NoSuchBeanDefinitionException e) {
			log.info("找不到redisTemplate，设置为不使用");
		} catch (Exception e) {
			log.info("找不到redisTemplate，设置为不使用");
		}
	}

	@Override
	protected void destory() {
		if(TaskPoolManager.getFgPool().containsTask(getId())) {
			if(getWay().equals(BatchFileDic.PW_FB_F)) {
				if(wholeTaskSuccess) {
					getCursor().resset();
				}
			}
		}
		super.destory();
	}
	
	@Override
	protected synchronized void finish() {
		super.finish();
		if(TaskPoolManager.getFgPool().containsTask(getId())) {
			//如果任务没成功完成，需记录到临时池查看
			if(isEndInMemoryTemp() || !wholeTaskSuccess) {
				if(redisTemplate != null) {
					redisTemplate.opsForValue().set(this.getId(), this, Duration.ofMinutes(60));
				}
			}
			TaskPoolManager.getFgPool().removeTask(this);
		}
	}

	public boolean isEndInMemoryTemp() {
		return endInMemoryTemp;
	}

	/**
	 * 结束后是否继续在内存保留一段时间，默认true，保留30分
	 * @param endInMemoryTemp
	 */
	public void setEndInMemoryTemp(boolean endInMemoryTemp) {
		this.endInMemoryTemp = endInMemoryTemp;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		if(redisTemplate != null) {
			this.endInMemoryTemp = true;
		}
	}

}
