package org.ljdp.component.permission;

import javax.servlet.http.HttpServletRequest;

public interface PermissionChecker {
	/**
	 * 判断用户是否有权限访问URL
	 * @param oprcode
	 * @param accessURI
	 * @return
	 * @throws Exception
	 */
	boolean checkURIPermission(String oprcode, String accessURI);

	/**
	 *  判断用户是否有权限访问资源
	 */
	boolean checkPermission(String oprcode, String resourceId);
	
	/**
	 * 判断是否受保护的URI
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	boolean isProtectedURI(String uri);
	
	/**
	 * 判断此URI的访问是否需要记录日志
	 * @param uri
	 * @return
	 */
	boolean isLoggerURI(String uri);
	
	boolean isLogin(HttpServletRequest request);
}
