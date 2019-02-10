package org.ljdp.support.web.sys;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.user.UserProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 游客默认登录信息
 * @author hzy
 *
 */
public class VisitorUserProvider implements UserProvider {

	@Override
	public DBAccessUser getUser() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest(); 
		DBAccessUser ljdpUser = new DBAccessUser();
		ljdpUser.setId("0");
		ljdpUser.setName("游客");
		ljdpUser.setAccount("visitor");
		ljdpUser.setIP(request.getRemoteAddr());
		return ljdpUser;
	}

}
