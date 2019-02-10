package org.ljdp.ui.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogFilter implements Filter {
	private static Logger log = LoggerFactory.getLogger(AccessLogFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("初始化AccessLogFilter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		String contextPath = hreq.getContextPath();
		String currentURI = hreq.getRequestURI(); // 当前要访问的Url
		if (currentURI != null) {
			currentURI = currentURI.replaceFirst(contextPath, "");
		}
		if (hreq.getQueryString() != null) {
			currentURI += "?" + hreq.getQueryString();
		}
		StringBuilder sb = new StringBuilder(2000);
		sb.append("\n");
		sb.append(hreq.getRemoteAddr()).append("\n");
		sb.append(hreq.getMethod() + " " + currentURI+"\n");
		boolean multiPart = false;
		String boundary = null;
		String charset = "UTF-8";
		Enumeration<String> headerNames = hreq.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = "";
			Enumeration<String> headers = hreq.getHeaders(headerName);
			while (headers.hasMoreElements()) {
				if(headerValue.length() > 0) {
					headerValue += ";";
				}
				headerValue += headers.nextElement();
			}
			if(!multiPart){
				if(headerName.equalsIgnoreCase("content-type") && 
						headerValue.indexOf("multipart/form-data")>=0){
					multiPart = true;
					String[] items = headerValue.split(";");
					for (int i = 0; i < items.length; i++) {
						if(items[i].indexOf("boundary") >= 0) {
							String[] boundaryItem = items[i].split("=");
							boundary = boundaryItem[1];
						}
						if(items[i].indexOf("charset") >= 0) {
							String[] valItems = items[i].split("=");
							charset = valItems[1];
						}
					}
				}
			}
			sb.append(headerName+": "+headerValue+"\n");
		}
		
		sb.append("\n");
		if(!multiPart){
//			sb.append(StringUtil.fromInputStream(request.getInputStream()));
			Enumeration<String> allNames =  request.getParameterNames();
			while (allNames.hasMoreElements()) {
				String param = (String) allNames.nextElement();
				String[] values = request.getParameterValues(param);
				if(values.length == 1) {
					sb.append(param).append("=").append(values[0]).append("\n");
				} else {
					sb.append(param).append("=").append(ArrayUtils.toString(values)).append("\n");
				}
			}
		} else{
			Iterator<Part> it = hreq.getParts().iterator();
			while (it.hasNext()) {
				Part part = (Part) it.next();
				sb.append("--").append(boundary).append("\n");
				boolean isBinary = false;
				String charset_part = charset;
				Iterator<String> headers = part.getHeaderNames().iterator();
				while (headers.hasNext()) {
					String headerName = (String) headers.next();
					String headerValue = "";
					Iterator<String> headerVals = part.getHeaders(headerName).iterator();
					while (headerVals.hasNext()) {
						String val = (String) headerVals.next();
						if(headerValue.length() > 0) {
							headerValue += ";";
						}
						headerValue += val;
					}
					if(!isBinary) {
						if(headerName.equals("content-transfer-encoding")
								&& headerValue.indexOf("binary") >= 0) {
							isBinary = true;
						} else if(headerName.equals("content-type")) {
							if(headerValue.contains("application/")
									|| headerValue.contains("image/")
									|| headerValue.contains("video/")
									|| headerValue.contains("audio/")) {
								isBinary = true;
							}
						}
						if(headerName.equals("content-type") && headerValue.indexOf("charset") >= 0) {
							String[] items = headerValue.split(";");
							for (int i = 0; i < items.length; i++) {
								if(items[i].indexOf("charset") >= 0) {
									String[] valItems = items[i].split("=");
									charset_part = valItems[1];
								}
							}
						}
					}
					sb.append(headerName+": "+headerValue+"\n");
				}
				if(!isBinary) {
					String value = IOUtils.toString(part.getInputStream(), charset_part);
					sb.append(value).append("\n");
				}
				sb.append("\n");
			}
		}
		sb.append("\n------------------------------------------------------------------------\n");
		log.info(sb.toString());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
