package com.openjava.framework.user;

import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.user.UserProvider;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;

/**
 * 获取登录用户的信息
 * @author hzy
 *
 */
public class CloudUserProvider implements UserProvider {

	@Override
	public DBAccessUser getUser() {
		DBAccessUser user = new DBAccessUser();
		Long userId = SsoContext.getUserId();
		if(userId != null) {
			user.setId(userId.toString());
		} else {
			Long passId = SsoContext.getPassId();
			if(passId != null) {
				user.setId(passId.toString());
			}
		}
		String acc = SsoContext.getAccount();
		user.setAccount(acc);
		user.setName(acc);
		Object u = SsoContext.getUser();
		if(u != null) {
			if(u instanceof UserVO) {
				BaseUserInfo uvo = (BaseUserInfo)u;
				user.setId(uvo.getUserId());
				user.setName(uvo.getUserName());
				user.setAccount(uvo.getUserAccount());
			}
		}
		return user;
	}

}
