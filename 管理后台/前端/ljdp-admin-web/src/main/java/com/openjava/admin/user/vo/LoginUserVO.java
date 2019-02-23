package com.openjava.admin.user.vo;

import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.plugin.sys.vo.UserVO;

public class LoginUserVO extends BasicApiResponse{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1003210689318271298L;
	private UserVO user;

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}
}
