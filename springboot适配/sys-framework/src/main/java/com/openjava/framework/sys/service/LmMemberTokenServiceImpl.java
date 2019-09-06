package com.openjava.framework.sys.service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.ljdp.component.sequence.UUIDHexSequence;
import org.ljdp.secure.cipher.SHA256;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.framework.sys.domain.LmMemberToken;
//import com.openjava.framework.sys.repository.LmMemberTokenRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
//@Transactional
public class LmMemberTokenServiceImpl implements LmMemberTokenService {
	
//	@Resource
//	private LmMemberTokenRepository msMemberTokenRepository;
//	
//	
	public LmMemberToken get(String id) {
//		Optional<LmMemberToken> m = msMemberTokenRepository.findById(id);
//		if(m.isPresent()) {
//			return m.get();
//		}
		return null;
	}
	
	public void save(LmMemberToken t) {
//		msMemberTokenRepository.save(t);
	}
	
	public List<LmMemberToken> findByPassIdAndAgent(Long passId,String userAgent){
//		return msMemberTokenRepository.findByPassIdAndAgent(passId, userAgent);
		return null;
	}
	
	public String buildLoginToken(Long userid, String userAgent) throws NoSuchAlgorithmException {
		//创建token
		String tokenId = createToken(userid, userAgent);
		
		//查找用户同一设备的登录记录
		List<LmMemberToken> loginList = findByPassIdAndAgent(userid, userAgent);
		loginList.forEach(mt -> {
			mt.setState("3");//退出登录
			save(mt);
		});
		
		//登录信息保存到数据库
		LmMemberToken mtoken = new LmMemberToken();
		mtoken.setTokenid(tokenId);
		mtoken.setPassId(userid);
		mtoken.setUserAgent(userAgent);
		mtoken.setLoginTime(new Date());
		mtoken.setState("1");
		save(mtoken);
		return tokenId;
	}

	public String createToken(Long userid, String userAgent) throws NoSuchAlgorithmException {
		UUIDHexSequence uuid = new UUIDHexSequence();
		String tokenId = uuid.getSequence()+userid+userAgent;
		tokenId = Base64.encodeBase64URLSafeString(SHA256.encode(tokenId));
		return tokenId;
	}
	
	public String createToken(String userCode, String userAgent) throws NoSuchAlgorithmException {
		UUIDHexSequence uuid = new UUIDHexSequence();
		String tokenId = uuid.getSequence()+userCode+userAgent;
		tokenId = Base64.encodeBase64URLSafeString(SHA256.encode(tokenId));
		return tokenId;
	}
}
