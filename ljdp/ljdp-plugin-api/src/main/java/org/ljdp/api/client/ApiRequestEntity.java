package org.ljdp.api.client;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ljdp.common.json.JacksonTools;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ApiRequestEntity {

	private String url;
	private HttpServletRequest request;
	private List<NameValuePair> params = new ArrayList<>(20);
	private String channel;
	private String token;
	private String userAgent;
	private Object body;
	
	public ApiRequestEntity(HttpServletRequest request) {
		super();
		this.request = request;
	}

	public void addParam(String name, String value) {
		if(StringUtils.isNotBlank(value)) {
			params.add(new BasicNameValuePair(name, value));
		}
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void joinParamsToURL() {
		if(params.size() > 0) {
			StringBuffer sb = new StringBuffer(1024);
			for (NameValuePair p : params) {
				if(sb.length() > 0) {
					sb.append("&");
				}
				sb.append(p.getName()).append("=").append(p.getValue());
			}
			if(url.indexOf("?") > 0) {
				url += "&";
			} else {
				url += "?";
			}
			url += sb.toString();
		}
	}
	
	public String getParam(String name) {
		for (NameValuePair p : params) {
			if(p.getName().equals(name)) {
				return p.getValue();
			}
		}
		return null;
	}
	
	public void removeParam(String name) {
		for (int i = 0; i < params.size(); i++) {
			NameValuePair p = params.get(i);
			if(p.getName().equals(name)) {
				params.remove(i);
				break;
			}
		}
	}
	
	public boolean containParam(String name) {
		for (NameValuePair p : params) {
			if(p.getName().equals(name)) {
				if(StringUtils.isNotBlank(p.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	public byte[] getBodyJsonContent() throws JsonProcessingException {
		return JacksonTools.getObjectMapper().writeValueAsBytes(this.body);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
