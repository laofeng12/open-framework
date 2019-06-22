package org.openjava.boot.conf.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ljdp.secure.aop.ValidAspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(5)
public class ParamsValidAOP {
	
	private ValidAspect aop = new ValidAspect();
	
	@Pointcut("execution(* com.openjava..api..*.*(..)))")  
	public void executeService(){
	  
	}
	
	@Around("executeService()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable{
		return aop.doValid(point);
	}

}
