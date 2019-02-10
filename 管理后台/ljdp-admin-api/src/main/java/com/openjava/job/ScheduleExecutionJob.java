package com.openjava.job;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.log.annotation.LogConfig;
import org.ljdp.log.annotation.LogSave;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.openjava.admin.job.domain.SysJobSchedule;
import com.openjava.admin.job.domain.SysJobWaitQueue;
import com.openjava.admin.job.query.SysJobWaitQueueDBParam;
import com.openjava.admin.job.service.SysJobScheduleService;
import com.openjava.admin.job.service.SysJobWaitQueueService;

/**
 * 检查待执行的计划任务，然后执行
 * @author hzy0769
 *
 */
@Component
public class ScheduleExecutionJob {
	
	@Resource
	private SysJobWaitQueueService sysJobWaitQueueService;
	@Resource
	private SysJobScheduleService sysJobScheduleService;

	@Scheduled(cron="${schedule.execution.waitqueue}")
	public void cronJob() throws Exception{
		{			
			//查询待执行的队列
			SysJobWaitQueueDBParam params = new SysJobWaitQueueDBParam();
			params.setNull_scheduleTime(true);
			Pageable pageable = PageRequest.of(0, 30);
			List<SysJobWaitQueue> waitQueues = sysJobWaitQueueService.queryDataOnly(params, pageable);
			waitQueues.forEach(w -> {
				//准备执行
				scheduleJob(w);
			});
		}
		{			
			SysJobWaitQueueDBParam params2 = new SysJobWaitQueueDBParam();
			params2.setLe_scheduleTime(new Date());
			Pageable pageable = PageRequest.of(0, 30);
			List<SysJobWaitQueue> waitQueues2 = sysJobWaitQueueService.queryDataOnly(params2, pageable);
			waitQueues2.forEach(w -> {
				scheduleJob(w);
			});
		}
	}

	private Object scheduleJob(SysJobWaitQueue w) {
		SysJobSchedule s = sysJobScheduleService.get(w.getJobId());
		if(s == null) {
			return null;
		}
		try {				
			return executeJob(w, s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object executeJob(SysJobWaitQueue w, SysJobSchedule s)
			throws IllegalAccessException, InvocationTargetException, ClassNotFoundException {
//		String[] clsPaths = s.getJobClass().split("\\.");
//		String beanName = StringUtil.lowerFirst(clsPaths[clsPaths.length-1]);
		@SuppressWarnings("rawtypes")
		Class jobCls = Class.forName(s.getJobClass());
		@SuppressWarnings("unchecked")
		Object bean = SpringContextManager.getBean(jobCls);
		if(bean == null) {
			System.out.println("["+s.getJobClass()+"]找不到任务："+s.getJobClass());
			return null;
		}
		int paramsCount = 0;
		String[] jobParams = {};
		if(StringUtils.isNotBlank(w.getJobParams())) {
			jobParams = w.getJobParams().split(",");
			paramsCount = jobParams.length;
		}
		Method[] methods = jobCls.getMethods();
		for (Method method : methods) {
			//看是否支持计划任务模块
			boolean enableSchedule = false;
			for(Annotation ano : method.getAnnotations()){
	            if(ano.annotationType().equals(LogConfig.class)
	            		|| ano.annotationType().equals(Scheduled.class)
	            		|| ano.annotationType().equals(LogSave.class)){
	            	enableSchedule = true;
	                break;
	            }
	        }
//			System.out.println("--------------------------------");
//			System.out.println("paramsCount="+paramsCount);
//			System.out.println(method.getName());
//			System.out.println("getParameterCount="+method.getParameterCount());
//			System.out.println("enableSchedule="+enableSchedule);
			if(!enableSchedule) {
				continue;
			}
			if(method.getName().equals(s.getJobMethod()) &&
					method.getParameterCount() == paramsCount) {
				Object[] jobArgs = new Object[method.getParameterCount()];
				if(paramsCount > 0) {
					@SuppressWarnings("rawtypes")
					Class[] paramTypes = method.getParameterTypes();
					for (int i = 0; i < paramTypes.length; i++) {
						if((jobParams.length-1) >= i &&
								StringUtils.isNotBlank(jobParams[i]) &&
								!jobParams[i].equalsIgnoreCase("null")) {
							//此位置填写的参数非空
							if(paramTypes[i].equals(String.class)) {
								jobArgs[i] = jobParams[i];
							} else if(paramTypes[i].equals(Long.class)) {
								jobArgs[i] = new Long(jobParams[i]);
							} else if(paramTypes[i].equals(Integer.class)) {
								jobArgs[i] = new Integer(jobParams[i]);
							} else if(paramTypes[i].equals(Double.class)) {
								jobArgs[i] = new Double(jobParams[i]);
							} else if(paramTypes[i].equals(Float.class)) {
								jobArgs[i] = new Float(jobParams[i]);
							} else if(paramTypes[i].equals(Date.class)) {
								jobArgs[i] = DateFormater.praseDate(jobParams[i]);
							}
						} else {
							jobArgs[i] = null;
						}
					}
				}
				sysJobWaitQueueService.doDelete(w.getQueueNo());
				return method.invoke(bean, jobArgs);
			}
		}
		System.out.println("["+s.getJobClass()+"]找不到任务方法："+s.getJobMethod());
		System.out.println("注意：");
		System.out.println("1、方法必须有注解：@Scheduled或者@LogConfig");
		System.out.println("2、提交的[参数数量]  要等于 [执行的方法的参数数量]");
		return null;
	}
	
//	public static void main(String[] args) {
//		System.out.println("a,,".split(",").length);
//		System.out.println(",a,".split(",").length);
//		System.out.println(",,a".split(",").length);
//	}
}
