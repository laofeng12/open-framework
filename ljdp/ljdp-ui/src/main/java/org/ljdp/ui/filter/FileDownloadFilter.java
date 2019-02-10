package org.ljdp.ui.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.ui.bean.FileDownloadInfo;

/**
 * 下载文件时，设置浏览器端显示的文件名
 * @author hzy
 *
 */
public class FileDownloadFilter implements Filter {

	private List<FileDownloadInfo> fileMapList = new ArrayList<>();
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		//请求路径(不包含context)
		String path = getRequestPath(request);
		//设置浏览器下载显示的文件名称
		for (int i = 0; i < fileMapList.size(); i++) {
			FileDownloadInfo fdi = (FileDownloadInfo) fileMapList.get(i);
			if(path.contains(fdi.getPath())) {
				response.addHeader("Content-disposition", "attachment;filename="
						+ new String(fdi.getClientName().getBytes("GBK"), "iso-8859-1"));
			}
		}
		
		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		Enumeration<String> et = config.getInitParameterNames();
		while(et.hasMoreElements()) {
			String pn = et.nextElement();
			String pv = config.getInitParameter(pn);
			System.out.println("[文件下载名称映射]"+pn+"="+pv);
			fileMapList.add(new FileDownloadInfo(pn, pv));
		}

	}

	public String getRequestPath(HttpServletRequest request){
		String path = request.getRequestURI().substring(request.getContextPath().length());
		
		if("".equals(path)){
			return "/";
		}
		
		return path;
	}
}
