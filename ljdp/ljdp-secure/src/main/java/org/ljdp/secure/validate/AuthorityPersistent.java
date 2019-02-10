package org.ljdp.secure.validate;

/**
 * 授权认证信息持久化接口
 * @author hzy
 *
 */
public interface AuthorityPersistent {

	public void saveAuth(AuthInfo auth);
	
	public AuthInfo findByTokenid(String tokenid);
}
