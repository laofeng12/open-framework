package org.ljdp.plugin.sys.resp;

import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.user.BaseUserInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("登录结果")
public class LoginResp extends BasicApiResponse {
	private static final long serialVersionUID = 6642876446324689762L;
	
	private BaseUserInfo user;

	public BaseUserInfo getUser() {
		return user;
	}

	public void setUser(BaseUserInfo user) {
		this.user = user;
	}
}
