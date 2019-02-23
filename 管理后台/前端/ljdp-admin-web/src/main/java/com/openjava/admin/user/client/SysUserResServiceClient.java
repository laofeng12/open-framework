package com.openjava.admin.user.client;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.resp.ResourceResp;
import org.ljdp.plugin.sys.resp.SystemResp;
import org.ljdp.plugin.sys.service.LjdpResService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SysUserResServiceClient implements LjdpResService{

	@Override
	public ResourceResp findByUser(BaseUserInfo user) {
		//请求后端接口进行登录
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		String apiUrl = "/api/admin/res/sysRes/myResources";
		apientity.setUrl(apiUrl);
		apientity.addParam("userId", user.getUserId());
		
		try {
			ResourceResp resp = ApiClient.doGet(apientity, ResourceResp.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			ResourceResp resp = new ResourceResp();
			resp.setCode(500);
			resp.setMessage(e.getMessage());
			return resp;
		}
	}
	
	@Override
	public SystemResp findMySystem() {
		//请求后端接口进行登录
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		String apiUrl = "/api/admin/sys/sysSubsystem/mySubsys";
		apientity.setUrl(apiUrl);
		
		try {
			SystemResp resp = ApiClient.doGet(apientity, SystemResp.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			SystemResp resp = new SystemResp();
			resp.setCode(500);
			resp.setMessage(e.getMessage());
			return resp;
		}
		
	}
}
