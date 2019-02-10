package org.ljdp.secure.validate.impl;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.secure.validate.AuthInfo;
import org.ljdp.secure.validate.AuthorityPersistent;
import org.ljdp.secure.validate.SessionValidator;

import net.rubyeye.xmemcached.MemcachedClient;

/**
 * 使用memcached进行登陆session验证
 * @author hzy
 *
 */
public class LmcitySessionVaidator implements SessionValidator {
	private static String ACCOUNT_CACHEKEY_PRE = "AC_";//帐号在Cache中的key是PK_${tokenid}
	private static String PASSID_CACHEKEY_PRE = "PA_";//帐号在Cache中的key是PK_${tokenid}
	private static String UID_CACHEKEY_PRE = "UID_";//系统管理用户id
	private static String USERAGENT_PRE = "UA_";//保存用户登录时使用的设备信息
	
	@Override
	public ApiResponse validate(HttpServletRequest request, HttpServletResponse response, Security secure) {
		ApiResponse result = new BasicApiResponse(APIConstants.CODE_SUCCESS);
		try {
			AuthorityPersistent authPersist = null;
			if(StringUtils.isNotEmpty(secure.authorityPersistent())) {
				authPersist = (AuthorityPersistent)SpringContextManager.getBean(secure.authorityPersistent());
			}
			String tokenid = request.getParameter("tokenid");
			if(StringUtils.isNotEmpty(tokenid)) {
				SsoContext.setToken(tokenid);
				//验证session
				ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
				MemcachedClient c = manager.getCache(secure.cacheName());
				if(c != null) {
					Object passObj = c.get(PASSID_CACHEKEY_PRE + tokenid);
					if(authPersist != null && passObj == null) {
						//尝试从数据库获取
						AuthInfo a = authPersist.findByTokenid(tokenid);
						if(a != null) {
							if("1".equals(a.getState())) {
								passObj = a.getPassId();
								boolean f = c.add(PASSID_CACHEKEY_PRE + tokenid, 0, passObj);
								System.out.println("[LmcitySessionVaidator]从持久层重新载入token,pass="+a.getPassId()+",addCache="+f);
							}
						}
					}
					if(passObj == null) {
						Object accountObj = c.get(ACCOUNT_CACHEKEY_PRE+tokenid);
						if(accountObj == null) {
							result.setCode(APIConstants.CODE_AUTH_FAILD);
							result.setMessage("会话失效");
						} else {
							//用户账户
							String account = (String)accountObj;
							SsoContext.setAccount(account);
							//用户id
							Object sysUserId = c.get(UID_CACHEKEY_PRE+tokenid);
							if(sysUserId != null) {
								SsoContext.setUserId((Long)sysUserId);
							}
						}
					} else {
						Long passId = (Long)passObj;
						SsoContext.setPassId(passId);
					}
					String method = request.getMethod();
					String userAgent = request.getParameter("userAgent");
					
					if(StringUtils.isEmpty(userAgent)){
						result.setCode(APIConstants.CODE_AUTH_FAILD);
						result.setMessage("缺少设备信息");
					} else {
						if("GET".equals(method)) {
							userAgent = URLDecoder.decode(userAgent, "utf-8");
						}
						String memUa = c.get(USERAGENT_PRE+tokenid);
						if(authPersist != null && memUa == null) {
							//尝试重新载入useragent
							AuthInfo a = authPersist.findByTokenid(tokenid);
							if(a != null) {
								memUa = a.getUserAgent();
								boolean f = c.add(USERAGENT_PRE+tokenid, 0, memUa);
								System.out.println("[LmcitySessionVaidator]从持久层重新载入agent,pass="+a.getPassId()+",addCache="+f);
							}
						}
						if(memUa != null) {
							if(!memUa.equals(userAgent)) {
								int vi = userAgent.lastIndexOf(" v");
								if(vi > 0) {
									String userAgentNoVer = userAgent.substring(0, vi);
									if(!memUa.startsWith(userAgentNoVer)) {
										result.setCode(APIConstants.CODE_AUTH_FAILD);
										result.setMessage("设备认证失败");
									}
								} else {
									result.setCode(APIConstants.CODE_AUTH_FAILD);
									result.setMessage("设备认证失败");
								}
							}
						}
					}
				} else {
					result.setCode(APIConstants.CODE_SERVER_ERR);
					result.setMessage("获取登录信息失败："+secure.cacheName());
				}
			} else {
				result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
				result.setMessage("请登录后操作");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(APIConstants.CODE_SERVER_ERR);
			result.setMessage("session validate fail");
		}
		return result;
	}

}
