package org.ljdp.ui.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncodingFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 2331837741011300763L;
	private static Log log = LogFactory.getLog(EncodingFilter.class);
	private FilterConfig filterConfig;
    private String targetEncoding = "UTF-8";

    public void init(FilterConfig filterConfig) throws ServletException{
    	super.init();
        this.filterConfig = filterConfig;
        String encoding = filterConfig.getInitParameter("encoding");
        if(StringUtils.isNotEmpty(encoding)) {
        	this.targetEncoding = encoding;
        }
        log.info("targetEncoding="+targetEncoding);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException {
        try {
            request.setCharacterEncoding(targetEncoding);
            filterChain.doFilter(request, response);
        }
        catch (Exception sx) {
            filterConfig.getServletContext().log(sx.getMessage());
            sx.printStackTrace();
            throw new ServletException(sx);
        }
    }

    public void destroy() {
        filterConfig = null;
        targetEncoding = null;
    }
}
