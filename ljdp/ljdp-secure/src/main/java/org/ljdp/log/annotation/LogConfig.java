package org.ljdp.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogConfig {

	boolean save() default true;
	
	boolean print() default true;
	
	boolean saveParams() default true;
	
	String[] desensitizeParams() default {};//需要脱敏的参数字段
}
