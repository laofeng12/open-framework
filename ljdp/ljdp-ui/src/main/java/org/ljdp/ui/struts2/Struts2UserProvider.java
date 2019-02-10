package org.ljdp.ui.struts2;

import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.user.UserProvider;
import org.ljdp.ui.common.WebConstant;

import com.opensymphony.xwork2.ActionContext;

public class Struts2UserProvider implements UserProvider {

	public DBAccessUser getUser() {
		DBAccessUser user = (DBAccessUser) ActionContext.getContext()
				.getSession().get(WebConstant.SESSION_ATTRIBUTE_USER);
		if(user == null) {
			throw new RuntimeException("未登录");
		}
		return user;
	}

}
