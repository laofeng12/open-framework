package org.ljdp.core.spring.dds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.core.spring.dds.DynamicDataSourceContextHolder;
import org.ljdp.core.spring.dds.TargetDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceAspect {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public Object doAround(ProceedingJoinPoint point) throws Throwable{
		TargetDataSource tds = null;
		Method m = ((MethodSignature)point.getSignature()).getMethod();
		Method taretMethod = point.getTarget().getClass().getMethod(m.getName(), m.getParameterTypes());
		for(Annotation ano : taretMethod.getAnnotations()){
			if(ano.annotationType().equals(TargetDataSource.class)){
				tds = (TargetDataSource)ano;
			 }
		}
		if(tds != null) {
			if(logger.isDebugEnabled()) {
				Class<?> targetClass = point.getSignature().getDeclaringType();
				String iden = targetClass.getSimpleName() + "." + m.getName();
				logger.debug("["+iden+"] set datasource="+tds.value());
			}
			DynamicDataSourceContextHolder.setDataSourceType(tds.value());
		}
		Object rvt = point.proceed();
		DynamicDataSourceContextHolder.clearDataSourceType();
		return rvt;
	}
}
