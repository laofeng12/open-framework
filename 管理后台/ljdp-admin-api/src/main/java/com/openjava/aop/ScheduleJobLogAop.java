package com.openjava.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.log.annotation.LogConfig;
import org.ljdp.log.annotation.LogSave;
import org.ljdp.util.DateFormater;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.openjava.admin.job.domain.SysJobLog;
import com.openjava.admin.job.domain.SysJobSchedule;
import com.openjava.admin.job.service.SysJobLogService;
import com.openjava.admin.job.service.SysJobScheduleService;
import com.openjava.exception.NotFinishException;

@Component
public class ScheduleJobLogAop {
	
	@Resource
	private SysJobScheduleService sysJobScheduleService;
	@Resource
	private SysJobLogService sysJobLogService;


    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        boolean needLog = false, saveLog = false, saveTrack = true;
        //类
        Class<?> targetClass = point.getSignature().getDeclaringType();
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        for(Annotation ano : method.getAnnotations()){
        	if(ano.annotationType().equals(Scheduled.class)){
            	needLog = true;
            	saveLog = true;
            } else if(ano.annotationType().equals(LogSave.class)){
            	needLog = true;
            	saveLog = true;
            	LogSave cfg = (LogSave)ano;
            	saveTrack = cfg.track();
            } else if(ano.annotationType().equals(LogConfig.class)){
            	needLog = true;
            	LogConfig cfg = (LogConfig)ano;
            	saveLog = cfg.save();
            }
        }
        if (!needLog || !saveLog) {//没有打这些日志标签，或者主动设置了save=false,那么就不记录日志
        	return point.proceed();
        }
        String jobClass = targetClass.getName();
        String jobMethod = method.getName();

        //获取参数
        StringBuffer paramsBuf = new StringBuffer();
        Object[] args = point.getArgs();
        Parameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            if(args == null || args.length <= i) {
                break;
            }
            if(args[i] == null) {
                continue;
            }
            String val = null;
            if(args[i] instanceof java.util.Date){
                val = DateFormater.formatDatetime((Date)args[i]);
            } else {
                val = args[i].toString();
            }
            if (paramsBuf.length() > 0) {
                paramsBuf.append(",");
            }
            paramsBuf.append(p.getName()).append("=").append(val);
        }

//        System.out.println("clsName="+clsName);
//        System.out.println("funcName="+funcName);
//        System.out.println("params="+paramsBuf.toString());
        
        Object retVal = null;
        long status = 1;
        SysJobSchedule sjob = null;
        SysJobLog joblog = null;
        //获取任务信息
        sjob = sysJobScheduleService.findByJobClassAndJobMethod(jobClass, jobMethod);
        if(sjob == null) {
        	sjob = new SysJobSchedule();
        	sjob.setJobId(ConcurrentSequence.getInstance().getSequence(""));
        	sjob.setJobClass(jobClass);
        	sjob.setJobMethod(jobMethod);
        	sysJobScheduleService.doSave(sjob);
        } else {
        	if(sjob.getLastStartTime() != null) {        			
        		if(sjob.getLastEndTime() == null) {
        			throw new NotFinishException("["+jobClass+"("+jobMethod+")]上次开始时间为"+DateFormater.formatDatetime(sjob.getLastStartTime())+"的计划还在运行中，中断本次调度，如果需要强制执行，请set last_start_time=null");
        		}
        	}
        }
        try{
        	Date startTime = new Date();
        	sjob.setLastStartTime(startTime);
        	sjob.setLastEndTime(null);
        	sysJobScheduleService.doSave(sjob);
        	//添加日志
        	joblog = new SysJobLog();
        	joblog.setLogSeq(ConcurrentSequence.getInstance().getSequence());
        	joblog.setJobId(sjob.getJobId());
        	joblog.setJobParams(paramsBuf.toString());
        	joblog.setStatus(status);
        	joblog.setStartTime(startTime);
        	if(saveTrack) {
        		sysJobLogService.doSave(joblog);
        	}
        	
        	retVal = point.proceed();
        	status = 2;
        } catch (Exception e){
            e.printStackTrace();
            status = 3;
            if(joblog != null) {            	
            	StringWriter sw = new StringWriter();
            	e.printStackTrace(new PrintWriter(sw, true));
            	joblog.setErrorLog(sw.toString());
            }
        }
        if(joblog != null) {        	
        	joblog.setStatus(status);
        	joblog.setFinishTime(new Date());
//        	System.out.println("【Debug】-----------------------------------------------");
//        	System.out.println(joblog.toStringMultiLine());
//        	System.out.println("【Debug】-----------------------------------------------");
        	if(saveTrack) {
        		sysJobLogService.doSave(joblog);
        	}
        	sjob.setLastEndTime(joblog.getFinishTime());
        	sysJobScheduleService.doSave(sjob);
        }
        
        return retVal;
    }
}
