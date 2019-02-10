package com.openjava.framework.user;

import javax.annotation.Resource;

import org.ljdp.secure.validate.AuthInfo;
import org.ljdp.secure.validate.AuthorityPersistent;

import com.openjava.framework.sys.domain.LmMemberToken;
import com.openjava.framework.sys.service.LmMemberTokenService;

public class LmAuthorityPersistent implements AuthorityPersistent {

	@Resource
	private LmMemberTokenService msMemberTokenService;
	
	@Override
	public AuthInfo findByTokenid(String tokenid) {
		LmMemberToken mt = msMemberTokenService.get(tokenid);
		return mt;
	}

	@Override
	public void saveAuth(AuthInfo auth) {
		msMemberTokenService.save((LmMemberToken)auth);
	}

}
