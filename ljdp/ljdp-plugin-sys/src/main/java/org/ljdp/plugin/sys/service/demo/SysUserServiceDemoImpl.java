package org.ljdp.plugin.sys.service.demo;

import java.util.ArrayList;
import java.util.List;

import org.ljdp.plugin.sys.resp.LoginResp;
import org.ljdp.plugin.sys.service.LjdpUserService;
import org.ljdp.plugin.sys.vo.RoleVO;
import org.ljdp.plugin.sys.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 演示服务，不连数据库，返回一些测试数据
 * @author hzy
 *
 */
@Transactional
public class SysUserServiceDemoImpl implements LjdpUserService {

	@Override
	public LoginResp findUserByAccountAndPwd(String userAccount, String userPwd) {
		UserVO u = new UserVO(userAccount, userAccount, "测试账号");
		RoleVO r = new RoleVO("0","超级管理员");
		List<RoleVO> rlist = new ArrayList<>();
		rlist.add(r);
		u.setRoleList(rlist);
		LoginResp resp = new LoginResp();
		resp.setCode(200);
		resp.setUser(u);
		return resp;
	}

}
