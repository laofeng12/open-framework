package com.openjava.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.ljdp.ui.spring.BaseController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class BackConsoleController extends BaseController {
	
	public HttpSession getHttpSession() {
		HttpServletRequest request = getHttpRequest();
		HttpSession session = request.getSession(false);
		return session;
	}

	public HttpServletRequest getHttpRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		return request;
	}
	
}
