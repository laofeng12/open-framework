package com.openjava.framework.validate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.common.secure.AES;
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
import org.ljdp.util.LocalDateTimeUtils;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisSessionVaidator implements SessionValidator {
	private static final String TOKEN_NAME = "authority-token";
	private static final String Authorization = "Authorization";
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	private AES aes = null;
	private boolean debug = false;
	
	public RedisSessionVaidator() {
		
	}
	
	public RedisSessionVaidator(boolean debug) {
		super();
		this.debug = debug;
	}

	public RedisSessionVaidator(String apiSkey) {
		try {
			aes = new AES(apiSkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
			if(StringUtils.isEmpty(tokenid) || tokenid.equalsIgnoreCase("undefined")) {
				tokenid = getTokenFromCookies(request);
			}
			if(StringUtils.isEmpty(tokenid) || tokenid.equalsIgnoreCase("undefined")) {
				tokenid = request.getHeader(Authorization);
				if(tokenid != null) {
					tokenid = tokenid.replaceFirst("Bearer ", "");
				}
			}
			if(StringUtils.isEmpty(tokenid) || tokenid.equalsIgnoreCase("undefined")) {
				tokenid = request.getParameter("tokenid");
			}
			if(StringUtils.isNotEmpty(tokenid) && null == aes) {
				SsoContext.setToken(tokenid);
				//验证session
				BaseUserInfo user = (BaseUserInfo)redisTemplate.opsForValue().get(tokenid);
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
							
							redisTemplate.opsForValue().set(tokenid, user, 7, TimeUnit.DAYS);
						}
					}
				}
				if(user != null) {
					SsoContext.setUser(user);
					SsoContext.setAccount(user.getUserAccount());
					if(StringUtils.isNumeric(user.getUserId())) {
						SsoContext.setUserId(new Long(user.getUserId()));
					}
					if(!secure.validateUserAgent()) {
						return result;
					}
//					String method = request.getMethod();
					String userAgent = request.getHeader("user-agent");
//					if(StringUtils.isEmpty(userAgent)){
//						userAgent = request.getParameter("userAgent");
//						if("GET".equals(method)) {
//							userAgent = URLDecoder.decode(userAgent, "utf-8");
//						}
//					}
					if(StringUtils.isEmpty(userAgent)){
						result.setCode(APIConstants.CODE_AUTH_FAILD);
						result.setMessage("缺少设备信息");
					} else {
						String memUa = user.getUserAgent();
						if(debug) {
							System.out.println("[Token-Agent]"+memUa);
							System.out.println("[Request-Agent]"+userAgent);
						}
						if(!memUa.equals(userAgent)) {
							int vi = userAgent.lastIndexOf(" v");
							if(vi > 0) {
								String userAgentNoVer = userAgent.substring(0, vi);
								if(!memUa.startsWith(userAgentNoVer)) {
									result.setCode(APIConstants.CODE_AUTH_FAILD);
									result.setMessage("设备认证失败");
								}
							} else {
								//看是否浏览器对user-agent做了特殊处理
								String qihu360Ua = memUa + " QIHU 360SE";
								if(qihu360Ua.equals(userAgent)) {
									//是360浏览器在后面添加的标志，可以认为也是同一个设备，认证通过
								} else {
									result.setCode(APIConstants.CODE_AUTH_FAILD);
									result.setMessage("设备认证失败");
								}
							}
						} else {
//							System.out.println("[RedisSessionVaidator]session有效："+user);
							if(user.getRefreshTime() == null) {
								user.setRefreshTime(user.getLoginTime());
							}
							if(user.getExpireInMin() != null && user.getExpireInMin().intValue() > 0) {
								long halfTimeout = user.getExpireInMin() / 2;//超时时间的一半
								//有多长时间没有刷新
								long lastRefreshTime = LocalDateTimeUtils.betweenTwoTime(
										LocalDateTimeUtils.convertDateToLDT(user.getRefreshTime()), 
										LocalDateTime.now(), 
										ChronoUnit.MINUTES);
								if(lastRefreshTime > halfTimeout) {
									//刷新token的有效期限
									user.setRefreshTime(new Date());
									redisTemplate.opsForValue().set(tokenid, user, user.getExpireInMin(), TimeUnit.MINUTES);
								}
								
							}
							
						}
					}
				} else {
					result.setCode(APIConstants.CODE_AUTH_FAILD);
					result.setMessage("会话失效");
				}
			} else if(aes != null) {
				String authHead = request.getHeader(Authorization);
				if(StringUtils.isNotEmpty(authHead)) {
					authHead = authHead.replaceFirst("Bearer ", "");
					String userJson = aes.decryptBase64(authHead);
					UserVO user = JacksonTools.getObjectMapper().readValue(userJson, UserVO.class);
					SsoContext.setUser(user);
					SsoContext.setAccount(user.getUserAccount());
					SsoContext.setToken(authHead);
				} else {
					result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
					result.setMessage("请登录后操作");
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
