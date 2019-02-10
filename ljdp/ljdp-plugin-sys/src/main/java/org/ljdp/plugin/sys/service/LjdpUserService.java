package org.ljdp.plugin.sys.service;

import org.ljdp.plugin.sys.resp.LoginResp;

public interface LjdpUserService {

	public LoginResp findUserByAccountAndPwd(String userAccount,String userPwd);
}
