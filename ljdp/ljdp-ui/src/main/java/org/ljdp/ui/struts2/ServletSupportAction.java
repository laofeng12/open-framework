package org.ljdp.ui.struts2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.ljdp.common.config.Constant;
import org.ljdp.common.config.Env;

import com.opensymphony.xwork2.ActionSupport;

public class ServletSupportAction extends ActionSupport {
	private static final long serialVersionUID = 7954024381372409151L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;

	public HttpServletRequest getRequest() {
		if(request == null) {
			request = ServletActionContext.getRequest();
		}
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		if(response == null) {
			if(Env.current().runstatus().contains(Constant.Run.NOSERVER)) {
//				response = new TestServletResponse();
			} else {
			}
			response = ServletActionContext.getResponse();
		}
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getSession() {
		if(session == null) {
			session = getRequest().getSession();
		}
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	public String getBasePath() {
		String path = getRequest().getContextPath();
		String basePath = getRequest().getScheme()+"://"
			+getRequest().getServerName()+":"+getRequest().getServerPort()+path+"/";
		return basePath;
	}
}
