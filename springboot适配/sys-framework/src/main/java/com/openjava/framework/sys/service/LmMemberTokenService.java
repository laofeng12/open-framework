package com.openjava.framework.sys.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.framework.sys.domain.LmMemberToken;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface LmMemberTokenService {
	
	public LmMemberToken get(String id);
	
	public void save(LmMemberToken t);
	
	public List<LmMemberToken> findByPassIdAndAgent(Long passId,String userAgent);
	
	public String buildLoginToken(Long userid, String userAgent) throws NoSuchAlgorithmException;
	
	public String createToken(Long userid, String userAgent) throws NoSuchAlgorithmException;
	
	public String createToken(String userCode, String userAgent) throws NoSuchAlgorithmException;
}
