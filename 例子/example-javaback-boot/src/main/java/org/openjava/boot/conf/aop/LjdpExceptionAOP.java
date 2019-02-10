package org.openjava.boot.conf.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ljdp.secure.aop.ExceptionAspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class LjdpExceptionAOP {

private ExceptionAspect aop = new ExceptionAspect();
	
	@Pointcut("execution(* org.ljdp.support..controller..*.*(..))")
	public void executeService(){
	
	}
	
	@Around("executeService()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable{
		return aop.doException(point);
	}
}
