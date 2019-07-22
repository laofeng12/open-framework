package org.ljdp.secure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {
	boolean session() default false;
	
	boolean redirectLogin() default true;//是否强制跳转登录页
	
	boolean escape() default true;
	
	String cacheName() default "sessionCache";//session保存的缓存区
	
	String sessionValidator() default "sessionValidator";//验证session是否登录
	
	boolean filterJs() default true;
	
	boolean filterQuotes() default true;
	
	String authorityPersistent() default "authorityPersistent";//把session中的授权信息持久化保存接口
	
	String[] excludes() default {"queryString"};//排除的字段
	
	/**
	 * 是否验证userAgent与登录一致
	 * @return
	 */
	boolean validateUserAgent() default true;
	
	/**
	 * 允许访问的身份，如果没有设置表示不做限制，默认空。
	 * @return
	 */
	String[] allowIdentitys() default {};
	/**
	 * 允许访问的角色，如果没有设置表示不做限制，默认空。
	 * @return
	 */
	String[] allowRoles() default {};
	/**
	 * 允许访问的用户账号，如果没有设置表示不做限制，默认空。
	 * @return
	 */
	String[] allowUserAccounts() default {};
}
