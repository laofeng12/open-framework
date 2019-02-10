package org.ljdp.secure.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.permission.PermissionChecker;
import org.ljdp.component.session.ReqParameter;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.util.WebUtils;

/**
 * 默认的安全过滤器，配置了该过滤器后
 * 
 * @author Administrator
 *
 */
public class DefaultSecurityFilter implements Filter {

	public static final String AJAX_REQUEST_HEADER = "x-requested-with"; //$NON-NLS-1$
	public static final String AJAX_REQUEST_VALUE = "XMLHttpRequest"; //$NON-NLS-1$
	public static final String IGNORE_PATTERN_PARAM = "ignore-pattern"; //【配置】$NON-NLS-1$
	public static final String INCLUDE_PATTERN_PARAM = "include-pattern";//【配置】
	public static final String LOGIN_URL_PARAM = "login-url"; //【配置】$NON-NLS-1$
	public static final String PERMISSION_CHECK_CLS = "check-class";//【配置】权限检查的类名
	public static final String RETURN_POST_URL = "return-post-url"; //【配置】如果是post方式提交，登录后跳转的地址
	public static final String SERVER_DOMAIN = "server-domain";//【配置】网站域名，把return-url里的ip替换为此域名
	

	private String ignorePatternString;
	protected Pattern ignorePattern;

	private String includePatternString;
	protected Pattern includePattern;

	protected String loginUrl; // $NON-NLS-1$
	protected String encoding = "UTF-8"; //$NON-NLS-1$
	
	private List<PermissionChecker> checkList;//权限检查服务类
	
	private String returnPostUrl;//如果是post提交请求被登录截拦，那么需要先转到一个post提交页面再重新提交
	private String serverDomain = null;
	
	public void init(FilterConfig config) throws ServletException {
		// 获取ignorePattern参数
		ignorePatternString = config.getInitParameter(IGNORE_PATTERN_PARAM);
		if (StringUtils.isNotEmpty(ignorePatternString)) {
			ignorePatternString = trimSpace(ignorePatternString);
			System.out.println("[DefaultSecurityFilter]ignorePattern="+ignorePatternString);
			setIgnorePattern(ignorePatternString);
		}

		// 获取includePattern参数
		includePatternString = config.getInitParameter(INCLUDE_PATTERN_PARAM);
		if (StringUtils.isNotEmpty(includePatternString)) {
			includePatternString = trimSpace(includePatternString);
			System.out.println("[DefaultSecurityFilter]includePattern="+includePatternString);
			setIncludePattern(includePatternString);
		}

		// 获取loginUrl参数
		String loginUrl = config.getInitParameter(LOGIN_URL_PARAM);
		if (StringUtils.isNotEmpty(loginUrl)) {
			setLoginUrl(loginUrl);
			System.out.println("[DefaultSecurityFilter]login-url="+loginUrl);
		}
		
		returnPostUrl = config.getInitParameter(RETURN_POST_URL);
		System.out.println("[DefaultSecurityFilter]"+RETURN_POST_URL+"="+returnPostUrl);
		
		serverDomain = config.getInitParameter(SERVER_DOMAIN);
		System.out.println("[DefaultSecurityFilter]"+SERVER_DOMAIN+"="+serverDomain);
		
		checkList = new ArrayList<>();
		String checkClass = config.getInitParameter(PERMISSION_CHECK_CLS);
		if (StringUtils.isNotEmpty(checkClass)) {
			checkClass = trimSpace(checkClass);
			String[] items = checkClass.split(",");
			try {
				for (int i = 0; i < items.length; i++) {
					PermissionChecker checker = (PermissionChecker)Class.forName(items[i]).newInstance();
					System.out.println("[DefaultSecurityFilter]checkClass="+items[i]);
					checkList.add(checker);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String trimSpace(String pattern) {
		pattern = pattern.replaceAll("\r", "").replaceAll("\n", "")
				.replaceAll(" ", "").replaceAll("\t", "");
		return pattern;
	}

	private void setIgnorePattern(String pattern) {
		if (!StringUtils.isEmpty(pattern)) {
			pattern = pattern.replaceAll("\\.", "\\\\."); // 替换 //$NON-NLS-1$ //$NON-NLS-2$
															// . 为正则表达式的写法 \.
			pattern = pattern.replaceAll("\\*", ".*"); // 替换 //$NON-NLS-1$ //$NON-NLS-2$
														// * 为正则表达式的写法 .*

			ignorePattern = Pattern.compile(pattern);
		}
	}

	private void setIncludePattern(String pattern) {
		if (!StringUtils.isEmpty(pattern)) {
			pattern = pattern.replaceAll("\\.", "\\\\."); // 替换 //$NON-NLS-1$ //$NON-NLS-2$
															// . 为正则表达式的写法 \.
			pattern = pattern.replaceAll("\\*", ".*"); // 替换 //$NON-NLS-1$ //$NON-NLS-2$
														// * 为正则表达式的写法 .*

			includePattern = Pattern.compile(pattern);
		}
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		doSecurityFilter(servletRequest, servletResponse, chain);
	}

	protected void doSecurityFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		// 初始化上下文
		try {
			// 请求路径(不包含context)
			String path = WebUtils.getRequestPath(request);

			// 获取当前认证用户对象
			if (!isLoginCheck(request)) {
				// 判断请求是否被忽略
				if (ignores(path) || isLoginPath(path)) {
					chain.doFilter(servletRequest, response);
					return;
				}

				handleNotAuthenticated(request, response);
			} else {

				// 通过安全验证，继续访问用户请求的地址
				chain.doFilter(servletRequest, servletResponse);
			}
		} finally {
		}
	}
	
	private boolean isLoginCheck(HttpServletRequest request) {
		if(checkList.size() > 0) {
			for (int i = 0; i < checkList.size(); i++) {
				PermissionChecker checker = (PermissionChecker) checkList.get(i);
				if(!checker.isLogin(request)) {
					return false;//只有有一个验证不通过，就不通过
				}
			}
			return true;
		}
		return SsoContext.authenticate(request);
	}

	/**
	 * 处理用户未登录的情况，跳转到SSO的登录页面
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void handleNotAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (isAjax(request)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else if(StringUtils.isNotEmpty(this.loginUrl)){
			String referer=request.getHeader("referer");
			if(referer == null) {
				referer = "";
			}
			String returnUrl = null;
			String showparams = "";
			if(request.getMethod().equalsIgnoreCase("GET")) {
				String requestUrl = request.getRequestURL().toString();
				String queryString = request.getQueryString();
				if (null != queryString) {
					returnUrl = requestUrl + "?" + queryString+"&referer="+URLEncoder.encode(referer, encoding);
				} else {
					returnUrl = requestUrl;
				}
				returnUrl = toServerReturnUrl(returnUrl);
				returnUrl = URLEncoder.encode(returnUrl, encoding);
			} else {
				List<ReqParameter> strideParams = new ArrayList<>();
				Enumeration<String> en = request.getParameterNames();
				while (en.hasMoreElements()) {
					String name = en.nextElement();
					String value = request.getParameter(name);
//					System.out.println(name+"="+value);
//					value = new String(value.getBytes("iso-8859-1"), encoding);
					ReqParameter p = new ReqParameter(name,value);
					strideParams.add(p);
				}
				String requestUrl = request.getRequestURL().toString();
				requestUrl = toServerReturnUrl(requestUrl);
				HttpSession session = request.getSession();
				session.setAttribute(SsoContext.STRIDE_SESSION_RETURN_URL, requestUrl);
				session.setAttribute(SsoContext.STRIDE_SESSION_PARAM, strideParams);
				session.setAttribute(SsoContext.STRIDE_SESSION_REFERER, referer);
				returnUrl = URLEncoder.encode(returnPostUrl, encoding);
				showparams = " post-return="+requestUrl+",referer="+referer;
			}
			String redirectUrl = WebUtils.appendQueryParam(request.getContextPath() + this.getLoginUrl(), "return_url", //$NON-NLS-1$
					returnUrl);
			System.out.println("login-redirect:"+redirectUrl+showparams);
			response.sendRedirect(redirectUrl);
		} else {
			String retJson = "{code: '4001', msg: 'Authentication is not through'}";
			response.setContentType("text/json");
			response.getWriter().write(retJson);
		}
	}

	protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		throw new RuntimeException("User can't visit url:" + request.getRequestURL().toString()); //$NON-NLS-1$
	}

	// 返回当前请求是否Ajax请求
	protected boolean isAjax(HttpServletRequest request) {
		// jQuery.ajax will send a header "X-Requested-With=XMLHttpRequest"
		return AJAX_REQUEST_VALUE.equals(request.getHeader(AJAX_REQUEST_HEADER));
	}

	/**
	 * 如果设置了include规则，且能匹配上，则需要受控 如果没有设置include规则，则看ignore规则是否能匹配上，如果能匹配上则放过
	 * 如果include和ignore都没有设置，则放过
	 */
	protected boolean ignores(String path) {
		if (includePattern != null) {
			return !(includePattern.matcher(path).matches());
		}
		if (ignorePattern != null) {
			return ignorePattern.matcher(path).matches();
		}
		return true;
	}

	protected boolean isLoginPath(String path) {
		return path.equals(this.getLoginUrl());
	}

	/**
	 * @return the ignorePatternString
	 */
	public String getIgnorePatternString() {
		return ignorePatternString;
	}

	/**
	 * @param ignorePatternString
	 *            the ignorePatternString to set
	 */
	public void setIgnorePatternString(String ignorePatternString) {
		this.ignorePatternString = ignorePatternString;
		setIgnorePattern(ignorePatternString);
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl.trim();
	}

	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		if (StringUtils.isEmpty(this.loginUrl)) {
			return "";
		}
		return this.loginUrl.startsWith("/") ? this.loginUrl : "/" + this.loginUrl;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the includePatternString
	 */
	public String getIncludePatternString() {
		return includePatternString;
	}

	/**
	 * @param includePatternString
	 *            the includePatternString to set
	 */
	public void setIncludePatternString(String includePatternString) {
		this.includePatternString = includePatternString;
		setIncludePattern(includePatternString);
	}

	public void destroy() {

	}
	
	/**
	 * 把地址转为真实的服务器地址，由于使用反向代理后转过来后，访问的域名变代理服务器的ip了
	 * @param returnUrl
	 * @return
	 */
	public String toServerReturnUrl(String returnUrl) {
		if(serverDomain != null) {
			int beginIndex = returnUrl.indexOf("//")+2;
			int endIndex = returnUrl.indexOf("/", beginIndex);
			String servername = returnUrl.substring(beginIndex, endIndex);
			return returnUrl.replaceFirst(servername, serverDomain);
		}
		return returnUrl;
	}
	
//	public static void main(String[] args) {
//		String url = "https://127.0.0.1:8090/m/order/offlineOrder";
//		int beginIndex = url.indexOf("//")+2;
//		int endIndex = url.indexOf("/", beginIndex);
//		System.out.println(beginIndex);
//		System.out.println(endIndex);
//		String servername = url.substring(beginIndex, endIndex);
//		System.out.println(servername);
//		url = url.replaceFirst(servername, "m.lmcity.com");
//		System.out.println(url);
//	}
}