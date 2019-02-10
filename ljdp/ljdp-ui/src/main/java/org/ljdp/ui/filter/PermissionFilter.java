package org.ljdp.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.permission.PermissionChecker;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.ui.common.WebConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionFilter implements Filter {
	private static Logger log = LoggerFactory.getLogger(PermissionFilter.class);
	
	private static String NO_LOGIN_PAGE = "/login.jsp"; 

	private static final String NO_PERM_PAGE = "/error/permission_deny.htm"; 

	public void init(FilterConfig filterConfig) throws ServletException {
		ConfigFile cfg = ConfigFileFactory.getInstance().get(Env.current().getCoreCfg());
		String loginPage = cfg.getValue("web.page.login");
		if(loginPage != null && loginPage.length() > 0) {
			NO_LOGIN_PAGE = loginPage;
		}
		log.info("NO_LOGIN_PAGE="+NO_LOGIN_PAGE);
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			HttpServletRequest hreq = (HttpServletRequest) request;
			HttpServletResponse hres = (HttpServletResponse) response;
			String contextPath = hreq.getContextPath();
			String currentURI = hreq.getRequestURI(); // 当前要访问的Url
			if (currentURI != null) {
				currentURI = currentURI.replaceFirst(contextPath, "");
			}
			if (hreq.getQueryString() != null) {
				currentURI += "?" + hreq.getQueryString();
			}

			PermissionChecker checker = (PermissionChecker)SpringContextManager.getBean("web." + PermissionChecker.class.getSimpleName());
			if(checker.isProtectedURI(currentURI)) {
				if(checker.isLoggerURI(currentURI)) {
					log.info("["+hreq.getMethod() + ": " +currentURI+"]");
				}
				DBAccessUser user = (DBAccessUser) hreq.getSession().getAttribute(WebConstant.SESSION_ATTRIBUTE_USER);
				if (user == null) {
					log.info("web request NotLogin: " + currentURI );
					hres.sendRedirect(contextPath + NO_LOGIN_PAGE);
					return;
				} else {
					if (!checker.checkURIPermission(user.getId(), currentURI)) {
						log.info("web request NoPermission: " + currentURI);
						hres.sendRedirect(contextPath + NO_PERM_PAGE);
						return;
					}
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception sx) {
			sx.printStackTrace();
			throw new ServletException(sx);
		}
	}
}
