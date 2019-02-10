package com.openjava.framework.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;


public class FrontExceptionAspect {

	public Object around(ProceedingJoinPoint point) throws Throwable
	{
		try {
			return point.proceed();
		} catch (Throwable e) {
			System.out.println("[ExceptionAspect]截获异常"+e.getMessage());
			e.printStackTrace();
			Method method = ((MethodSignature)point.getSignature()).getMethod();
//			boolean ajax = false;
//			for(Annotation ano : method.getAnnotations()){
//				 if(ano.annotationType().equals(ResponseBody.class)){
//					 ajax = true;
//					 break;
//				 }
//			}
			if(method.getReturnType().equals(ModelAndView.class)) {
				ModelAndView mav = new ModelAndView("/fail/exception");
				mav.addObject("message", e.getMessage());
				return mav;
			} else {
				RequestAttributes ra = RequestContextHolder.getRequestAttributes();
				HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
				response.setContentType("text/json;charset=UTF-8");
				BasicApiResponse api = new BasicApiResponse(500, e.getMessage());
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(response.getOutputStream(), api);
				return null;
			}
		}
	}
}
