package org.ljdp.secure.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.validation.groups.Default;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.component.exception.APIException;
import org.ljdp.secure.annotation.ValidateConfig;
import org.ljdp.util.ValidationResult;
import org.ljdp.util.ValidationUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 通用验证
 * @author hzy0769
 *
 */
public class ValidAspect {

	public Object doValid(ProceedingJoinPoint point) throws Throwable {
		Method method = ((MethodSignature)point.getSignature()).getMethod();
		ValidateConfig validateCfg = method.getAnnotation(ValidateConfig.class);
		boolean enable = true;
		Class<?>[] groups = {Default.class};
		if(validateCfg != null) {
			enable = validateCfg.enable();
			if(validateCfg.groups() != null) {
				groups = validateCfg.groups();
			}
		}
		if(enable) {
			//需要验证
			Parameter[] params = method.getParameters();
			Object[] args = point.getArgs();
			for (int i = 0; i < params.length; i++) {
				Parameter p = params[i];
				if(args == null || args.length <= i) {
					break;
				}
				if(args[i] == null) {
					continue;
				}
				RequestBody rbody = p.getAnnotation(RequestBody.class);
				if(rbody != null) {
					Object bodyValue = args[i];
					ValidationResult result = ValidationUtils.validateEntity(bodyValue, groups);
					if(result.isHasErrors()) {
						//验证失败
						StringBuilder sb = new StringBuilder(result.getErrorMsg().size()*30);
						result.getErrorMsg().forEach((k, v) -> {
							if(sb.length() > 0) {
								sb.append("\r\n");
							}
							sb.append("[").append(k).append("]").append(v);
						});
						throw new APIException(400, sb.toString());
					}
					break;
				}
			}
		}
		return point.proceed();
	}
}
