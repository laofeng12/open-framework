package org.openjava.boot.conf.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ljdp.component.result.APIConstants;
import org.ljdp.secure.aop.SecurityAspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(4)
public class ApiSecurityAOP {
	
	private SecurityAspect aop = new SecurityAspect();

	@Pointcut("execution(* com.openjava..api..*.*(..)))")  
	public void executeService(){
	  
	}
	
	@Around("executeService()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable{
//		aop.setResultPosition(APIConstants.HTTP_POSITION_HEAD);
		return aop.doAudit(point);
	}
}
