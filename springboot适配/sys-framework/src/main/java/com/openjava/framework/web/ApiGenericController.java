package com.openjava.framework.web;

import org.ljdp.secure.sso.SsoContext;

public class ApiGenericController {

	protected String getUserId() {
		Long passId = SsoContext.getPassId();
		String account = SsoContext.getAccount();
		if(passId != null) {
			return passId.toString();
		}
		return account;
	}
	
	protected Long getPassId() {
		return SsoContext.getPassId();
	}
	
	protected String getUserAccount() {
		return SsoContext.getAccount();
	}
}
