package com.openjava.admin.user.client;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.plugin.sys.resp.LoginResp;
import org.ljdp.plugin.sys.service.LjdpUserService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SysUserServiceClient implements LjdpUserService {

	@Override
	public LoginResp findUserByAccountAndPwd(String userAccount, String userPwd) {
		//请求后端接口进行登录
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		String apiUrl = "/api/admin/user/sysUser/login";
		apientity.setUrl(apiUrl);
		apientity.addParam("userAccount", userAccount);
		apientity.addParam("userPwd", userPwd);
		try {
			LoginResp resp = ApiClient.doPost(apientity, LoginResp.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			LoginResp resp = new LoginResp();
			resp.setCode(500);
			resp.setMessage(e.getMessage());
			return resp;
		}
	}

}
