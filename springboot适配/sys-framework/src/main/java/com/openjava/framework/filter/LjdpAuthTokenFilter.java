package com.openjava.framework.filter;

import java.io.IOException;

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

public class LjdpAuthTokenFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
//		HttpServletResponse hresp = (HttpServletResponse)response;
		String tokenid = hreq.getParameter("tokenid");
		if(StringUtils.isNotEmpty(tokenid)) {
			HttpSession session = hreq.getSession();
//			Object token = session.getAttribute("security.auth.token");
			session.setAttribute("security.auth.token", tokenid);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
