package com.openjava.admin.user.client;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.plugin.sys.resp.LoginResp;
import org.ljdp.plugin.sys.service.LjdpUserService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openjava.admin.user.vo.LoginUserVO;

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
			LoginUserVO userResult = ApiClient.doPost(apientity, LoginUserVO.class);
			LoginResp resp = new LoginResp();
			resp.setCode(userResult.getCode());
			resp.setMessage(userResult.getMessage());
			resp.setUser(userResult.getUser());
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
