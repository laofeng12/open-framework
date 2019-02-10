package org.ljdp.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {

    public static String getRequestPath(HttpServletRequest request){
		String path = request.getRequestURI().substring(request.getContextPath().length());
		
		if("".equals(path)){
			return "/";
		}
		
		return path;
	}

    public static String appendQueryParam(String url,String param,String value){
		StringBuilder sb = new StringBuilder(url);
		if(url.contains("?")){
			sb.append("&");
		}else{
			sb.append("?");
		}
		sb.append(param).append("=").append(value);
		return sb.toString();
	}
}
