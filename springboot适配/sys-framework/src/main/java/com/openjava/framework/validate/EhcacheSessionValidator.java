package com.openjava.framework.validate;

import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.secure.validate.AuthInfo;
import org.ljdp.secure.validate.AuthorityPersistent;
import org.ljdp.secure.validate.SessionValidator;
import org.ljdp.support.dictionary.DictConstants;

public class EhcacheSessionValidator implements SessionValidator {
	private static final String TOKEN_NAME = "authority-token";

	private String getTokenFromCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals(TOKEN_NAME)) {
					if(StringUtils.isNotEmpty(cookies[i].getValue())) {
						return cookies[i].getValue();
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public ApiResponse validate(HttpServletRequest request, HttpServletResponse response, Security secure) {
		ApiResponse result = new BasicApiResponse(APIConstants.CODE_SUCCESS);
		try {
			AuthorityPersistent authPersist = null;
			if(StringUtils.isNotEmpty(secure.authorityPersistent())) {
				authPersist = (AuthorityPersistent)SpringContextManager.getBean(secure.authorityPersistent());
			}
			String tokenid = request.getHeader(TOKEN_NAME);
			if(StringUtils.isEmpty(tokenid)) {
				tokenid = getTokenFromCookies(request);
			}
			if(StringUtils.isEmpty(tokenid)) {				
				tokenid = request.getParameter("tokenid");
			}
			if(StringUtils.isNotEmpty(tokenid)) {
				SsoContext.setToken(tokenid);
				//验证session
				BaseUserInfo user = (BaseUserInfo)MemoryCache.getData(DictConstants.CACHE_SESSION, tokenid);
				if(authPersist != null && user == null) {
					//尝试从数据库获取
					AuthInfo a = authPersist.findByTokenid(tokenid);
					if(a != null) {
						if("1".equals(a.getState())) {
							BaseUserInfo vo = new UserVO();
							vo.setUserId(a.getPassId().toString());
//							vo.setUserAccount(a.get);
//							vo.setUserName(u.getFullname());
//							vo.setUserMobileno(a.get);
//							vo.setHeadImg(a.get);
							vo.setUserType("member");
							vo.setUserAgent(a.getUserAgent());
							vo.setLoginTime(a.getLoginTime());
							
							MemoryCache.putData(DictConstants.CACHE_SESSION, tokenid, vo);
						}
					}
				}
				if(user != null) {
					SsoContext.setUser(user);
					SsoContext.setAccount(user.getUserAccount());
					if(StringUtils.isNumeric(user.getUserId())) {
						SsoContext.setUserId(new Long(user.getUserId()));
					}
					String method = request.getMethod();
					String userAgent = request.getParameter("userAgent");
					if(StringUtils.isEmpty(userAgent)){
						userAgent = request.getHeader("user-agent");
					}
					if(StringUtils.isEmpty(userAgent)){
						result.setCode(APIConstants.CODE_AUTH_FAILD);
						result.setMessage("缺少设备信息");
					} else {
						if("GET".equals(method)) {
							userAgent = URLDecoder.decode(userAgent, "utf-8");
						}
						String memUa = user.getUserAgent();
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
						} else {
//							System.out.println("[RedisSessionVaidator]session有效："+user);
						}
					}
				} else {
					result.setCode(APIConstants.CODE_AUTH_FAILD);
					result.setMessage("会话失效");
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
