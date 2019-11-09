package org.ljdp.secure.sso;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.ljdp.component.result.ApiResponse;

public class SsoContext {

	private static final String SIGNIN_USER = SsoContext.class.getName() + "$user";
	private static final String SIGNIN_TOKEN = "security.auth.token";
	
	public static final String STRIDE_SESSION_PARAM = "stride.session.param";
	public static final String STRIDE_SESSION_RETURN_URL = "stride.session.return_url";
	public static final String STRIDE_SESSION_REFERER = "stride.session.referer";

	private static final ThreadLocal<Long> passIdHolder = new ThreadLocal<>();
	private static final ThreadLocal<String> accountHolder = new ThreadLocal<>();
	private static final ThreadLocal<ApiResponse> resultHolder = new ThreadLocal<>();
	private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
	private static final ThreadLocal<Long> requestIdHolder = new ThreadLocal<>();
	private static final ThreadLocal<String> tubHolder = new ThreadLocal<>();
	private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
	private static final ThreadLocal<Object> userHolder = new ThreadLocal<>();
	private static final ThreadLocal<Set<String>> resourcesHolder = new ThreadLocal<>();

	public static boolean authenticate(HttpServletRequest request) {
		Object u = getSignedInUserFromSession(request);
		return u != null? true : false;
	}

	/**
	 * 执行登录操作，将被验证的有用户Id写入Session中
	 * 
	 * @param request
	 * @param loginId
	 * @return
	 */
	public static Object signIn(HttpServletRequest request, Object user, String token) {
		storeSignedInUserInSession(request, user);
		if(token != null) {
			request.getSession(true).setAttribute(SIGNIN_TOKEN, token);
		}
		return user;
	}
	
	public static Object signIn(HttpServletRequest request, Object user) {
		return signIn(request, user, null);
	}

	public static void storeSignedInUserInSession(HttpServletRequest request, Object user) {
		request.getSession(true).setAttribute(SIGNIN_USER, user);
	}

	public static Object getSignedInUserFromSession(HttpServletRequest request) {
		return request.getSession(true).getAttribute(SIGNIN_USER);
	}
	
	public static String getSignedInTokenFromSession(HttpServletRequest request) {
		return (String)request.getSession(true).getAttribute(SIGNIN_TOKEN);
	}

	public static void singOut(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
	}
	
	public static void setPassId(Long passId) {
		passIdHolder.set(passId);
	}
	public static Long getPassId() {
		return passIdHolder.get();
	}
	public static void removePassId() {
		passIdHolder.remove();
	}
	
	public static void setUserId(Long userId) {
		userIdHolder.set(userId);
	}
	public static Long getUserId() {
		return userIdHolder.get();
	}
	public static void removeUserId() {
		userIdHolder.remove();
	}
	
	public static void setAccount(String account) {
		accountHolder.set(account);
	}
	public static String getAccount() {
		return accountHolder.get();
	}
	public static void removeAccount() {
		accountHolder.remove();
	}
	
	public static void setApiResponse(ApiResponse resp) {
		resultHolder.set(resp);
	}
	public static ApiResponse getApiResponse() {
		return resultHolder.get();
	}
	public static void removeApiResponse() {
		resultHolder.remove();
	}
	
	public static Long getRequestId() {
		return requestIdHolder.get();
	}
	public static void setRequestId(Long reqId) {
		requestIdHolder.set(reqId);
	}
	
	public static void setTub(String tub) {
		tubHolder.set(tub);
	}
	
	public static String getTub() {
		return tubHolder.get();
	}
	
	public static void setToken(String token) {
		tokenHolder.set(token);
	}
	
	public static String getToken() {
		return tokenHolder.get();
	}
	
	public static void setUser(Object user) {
		userHolder.set(user);
	}
	
	public static Object getUser() {
		return userHolder.get();
	}
	
	public static void setResources(Set<String> resources) {
		resourcesHolder.set(resources);
	}
	
	public static Set<String> getResources(){
		return resourcesHolder.get();
	}
	
	public static void clearContext() {
		passIdHolder.remove();
		accountHolder.remove();
		resultHolder.remove();
		userIdHolder.remove();
		requestIdHolder.remove();
		tubHolder.remove();
		tokenHolder.remove();
		userHolder.remove();
		resourcesHolder.remove();
	}
}
