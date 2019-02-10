package org.ljdp.ui.spring;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.common.config.Constant;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.user.UserProvider;
import org.ljdp.component.user.UserRoleChecker;
import org.ljdp.util.DateFormater;
import org.ljdp.util.StringUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {
	
	public static String VER_JS = DateFormater.formatDatetime_SHORT(new Date());
	public static String VER_CSS = DateFormater.formatDatetime_SHORT(new Date());

	public DBAccessUser getUser() {
		String upid = "web.UserProvider";
		if (Env.current().runstatus().contains(Constant.Run.NOSERVER)) {
			upid = "system.UserProvider";
		}
		if (Env.current().runstatus().contains(Constant.Run.NOLOGIN)) {
			upid = "web.visitor.UserProvider";
		}
		UserProvider userProvider = (UserProvider) SpringContextManager
				.getBean(upid);
		return userProvider.getUser();
	}

	public boolean isAdmin() {
		UserRoleChecker checker = (UserRoleChecker) SpringContextManager
				.getBean("system.UserRoleChecker");
		if (checker != null) {
			return checker.isAdministrator(getUser());
		}
		return false;
	}

	protected ModelAndView getAutoView()  {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		String requestURI = request.getRequestURI();
		// 处理RequestURI
		String contextPath = request.getContextPath();

		requestURI = requestURI.replace(".ht", "");
		int cxtIndex = requestURI.indexOf(contextPath);
		if (cxtIndex != -1) {
			requestURI = requestURI.substring(cxtIndex + contextPath.length());
		}

		String[] paths = requestURI.split("[/]");
		StringBuffer sb = new StringBuffer(60);
		for (int i = 0; i < paths.length-2; i++) {
			sb.append("/").append(paths[i]);
		}
		sb.append("/").append(paths[paths.length-2])
			.append(StringUtil.upperFirst(paths[paths.length-1]));
		String jspPath = sb.toString();
//		System.out.println(jspPath);
		ModelAndView mav = new ModelAndView(jspPath);
		mav.addObject("jver", VER_JS);
		mav.addObject("cver", VER_CSS);
		return mav;
	}
}
