package org.ljdp.secure.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.util.BeanEscapeUtil;
import org.springframework.web.servlet.ModelAndView;

public class SpringMvcSecurityAspect {
public static Map<String,Security> annomap = new HashMap<String,Security>();
	
	public Object doAudit(ProceedingJoinPoint point) throws Throwable {
		String iden = point.getSignature().toString();
		
		if(!annomap.containsKey(iden)){
			Method m = ((MethodSignature)point.getSignature()).getMethod();
			for(Annotation ano : m.getAnnotations()){
				if(ano.annotationType().equals(Security.class)){
					 annomap.put(iden, (Security) ano);
					 break;
				 }
			}
			if(!annomap.containsKey(iden)){
				annomap.put(iden, null);
			}
		}
		
		Security secure = annomap.get(iden);
		Object[] args = point.getArgs();
		Object[] afterSecurityArgs = args;
		boolean escape = true;
		boolean filterJs = true;
		boolean filterQuotes = true;
		boolean forceSecure = false;
		Set<String> excludeSet = new HashSet<>();
		if(secure != null){
			if(!secure.escape()) {
				escape = false;
			} else {
				forceSecure = true;
			}
			if(!secure.filterJs()) {
				filterJs = false;
			}
			if(!secure.filterQuotes()) {
				filterQuotes = false;
			}
			if(secure.excludes() != null) {
				for (String e : secure.excludes()) {
					excludeSet.add(e);
				}
			}
		}
		Object result = point.proceed(afterSecurityArgs);
		if(escape && result instanceof ModelAndView) {
			ModelAndView mv = (ModelAndView)result;
			Iterator<String> it = mv.getModelMap().keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object val = mv.getModelMap().get(key);
				if(null == val) {
					continue;
				}
				if(excludeSet.contains(key)) {
					continue;
				}
				if(val instanceof String) {
					val = BeanEscapeUtil.escapeIfNotSecurity(point.getTarget().getClass(),
							key, val.toString(), forceSecure, filterJs, filterQuotes);
					mv.getModelMap().put(key, val);
				} else {
					BeanEscapeUtil.escapeBean(val, forceSecure, filterJs, filterQuotes);
				}
			}
		}
		return result;
	}
	
}
