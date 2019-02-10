package org.ljdp.ui.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.ui.struts2.BaseAction;

public abstract class AbstractWebBusinessObject implements BusinessObject {

	public Object doBusiness(Object[] params) throws Exception {
		HttpServletRequest request = (HttpServletRequest)params[0];
		HttpServletResponse response = (HttpServletResponse)params[1];
		HttpSession session = (HttpSession)params[2];
		DBAccessUser user = (DBAccessUser)params[3];
		BaseAction action = (BaseAction)params[4];
		return doBusiness(request, response, session, user, action);
	}
	
	public abstract String doBusiness(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, DBAccessUser user, BaseAction actionScope) throws Exception;

}
