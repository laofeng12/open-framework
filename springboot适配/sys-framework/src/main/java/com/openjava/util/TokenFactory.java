package com.openjava.util;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.ljdp.component.exception.APIException;
import org.ljdp.secure.cipher.SHA256;

import net.rubyeye.xmemcached.MemcachedClient;

public class TokenFactory {
	private static String PASSID_CACHEKEY_PRE = "PA_";
	private static String USERAGENT_PRE = "UA_";//保存用户登录时使用的设备信息

	public static String buildTokenId(Long passId, String userAgent) throws Exception {
		if(passId == null || passId.longValue() == 0) {
			throw new APIException(-1001, "缺少用户id");
		}
		if(StringUtils.isBlank(userAgent)) {
			throw new APIException(-1002, "缺少设备信息");
		}
		UUID uuid = UUID.randomUUID();
		String tokenid  = uuid.toString();
		tokenid = SHA256.encodeAsString(tokenid);
		
		int exp = 24*60*60;
		
		ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
		MemcachedClient c = manager.getCache("sessionCache");
		Object passObj = c.get(PASSID_CACHEKEY_PRE + tokenid);
		if(passObj != null) {
			System.out.println("Token冲突:"+tokenid+","+passId);
			throw new Exception("获取令牌失败");
		}
		
		c.set(PASSID_CACHEKEY_PRE + tokenid, exp, passId);
		c.set(USERAGENT_PRE+tokenid, exp, userAgent);
		
		return tokenid;
	}
}
