package org.ljdp.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogSave {
	/**
	 * 是否持久化保存轨迹
	 * 对于频繁执行的job，例如几秒一次的，建议不要保存
	 * @return
	 */
	boolean track() default true;
}
