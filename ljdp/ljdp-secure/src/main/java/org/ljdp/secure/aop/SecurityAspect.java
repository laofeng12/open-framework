package org.ljdp.secure.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.secure.annotation.IgnoreEscape;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.secure.util.BeanEscapeUtil;
import org.ljdp.secure.validate.SessionValidator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 安全处理
 * @author hzy
 *
 */
public class SecurityAspect {
	
	public static Map<String,Security> annomap = new HashMap<String,Security>();
	public int resultPosition = APIConstants.HTTP_POSITION_BODY;
	
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
		if(secure != null){
			//验证是否登录
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
			HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
			SessionValidator validator = null;
			if(StringUtils.isNotEmpty(secure.sessionValidator())) {
				validator = (SessionValidator)SpringContextManager.getBean(secure.sessionValidator());
			}
			if(validator != null) {
				ApiResponse result = validator.validate(request, response, secure);
				if (secure.session()) {
					//需要验证
					if(result.getCode().intValue() != APIConstants.CODE_SUCCESS) {
						System.out.println("["+iden+"]"+result.getCode()+":"+result.getMessage());
						if(!secure.redirectLogin()) {
							result.setCode(APIConstants.ACCESS_NO_USER);//改为不需要强制登录
						}
						SsoContext.setApiResponse(result);
						if(APIConstants.HTTP_POSITION_HEAD == resultPosition) {
							response.setStatus(result.getCode());
							response.addHeader("message", result.getMessage());
						} else {
							JacksonTools.writePage(result, response);
						}
						return null;
					}
				}
			}
			if(secure.escape()) {
				afterSecurityArgs = new Object[args.length];
				Method method = ((MethodSignature)point.getSignature()).getMethod();
				Parameter[] params = method.getParameters();
				for (int i = 0; i < args.length; i++) {
					Object pv = args[i];
					if(pv instanceof String) {
						Parameter p = params[i];
						IgnoreEscape ignore = p.getAnnotation(IgnoreEscape.class);
						if(ignore != null) {
							if(pv != null) {
								pv = BeanEscapeUtil.filterScript(pv.toString());
							}
							afterSecurityArgs[i] = pv;
						} else {
							afterSecurityArgs[i] = StringEscapeUtils.escapeHtml4((String)pv);
						}
					} else {
						BeanEscapeUtil.escapeBean(pv, true, false, false);
						afterSecurityArgs[i] = pv;
					}
				}
			}
		}
		Object result = point.proceed(afterSecurityArgs);
//		System.out.println("结束：SecurityAspect");
		if(result != null) {
			if(result instanceof ApiResponse) {
				ApiResponse apir = (ApiResponse)result;
				if(apir.getCode() == null) {
					apir.setCode(APIConstants.CODE_SUCCESS);
				}
			}
		}
		return result;
	}

	public int getResultPosition() {
		return resultPosition;
	}

	public void setResultPosition(int resultPosition) {
		this.resultPosition = resultPosition;
	}
}
