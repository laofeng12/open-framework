package com.openjava.framework.validate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
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
import org.ljdp.secure.validate.AuthorityPersistent;
import org.ljdp.secure.validate.SessionValidator;
import org.ljdp.util.LocalDateTimeUtils;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class RedisSessionVaidator implements SessionValidator {
	private static final String TOKEN_NAME = "authority-token";
	private static final String Authorization = "Authorization";
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	private AES aes = null;
	private boolean debug = false;
	private List<String> defaultAllowIdentitys = new ArrayList<>();//默认全局允许的身份
	
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
	
	public void addAllowIdentity(String identity) {
		defaultAllowIdentitys.add(identity);
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
			if(StringUtils.isNotEmpty(tokenid)) {
				SsoContext.setToken(tokenid);
				//验证session
				BaseUserInfo user = (BaseUserInfo)redisTemplate.opsForValue().get(tokenid);
				if(user == null) {
					//尝试从数据库获取
					AuthorityPersistent authPersist = null;
					if(StringUtils.isNotEmpty(secure.authorityPersistent())) {
						authPersist = (AuthorityPersistent)SpringContextManager.getBean(secure.authorityPersistent());
					}
					if(authPersist != null) {
						BaseUserInfo dbuser = authPersist.getUserByToken(tokenid);
						if(dbuser != null) {
							user = dbuser;
							redisTemplate.opsForValue().set(tokenid, dbuser, dbuser.getExpireInMin(), TimeUnit.MINUTES);
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
					boolean validateUserAgent = secure.validateUserAgent();//是否验证userAgent
					if(validateUserAgent && StringUtils.isEmpty(userAgent)){
						result.setCode(APIConstants.CODE_AUTH_FAILED);
						result.setMessage("缺少设备信息");
					} else {
						String memUa = user.getUserAgent();
						if(debug) {
							System.out.println("[Token-Agent]"+memUa);
							System.out.println("[Request-Agent]"+userAgent);
						}
						if(validateUserAgent && !memUa.equals(userAgent)) {
							int vi = userAgent.lastIndexOf(" v");
							if(vi > 0) {
								String userAgentNoVer = userAgent.substring(0, vi);
								if(!memUa.startsWith(userAgentNoVer)) {
									result.setCode(APIConstants.CODE_AUTH_FAILED);
									result.setMessage("设备认证失败");
								}
							} else {
								//看是否浏览器对user-agent做了特殊处理
								String qihu360Ua = memUa + " QIHU 360SE";
								if(qihu360Ua.equals(userAgent)) {
									//是360浏览器在后面添加的标志，可以认为也是同一个设备，认证通过
								} else {
									result.setCode(APIConstants.CODE_AUTH_FAILED);
									result.setMessage("设备认证失败");
								}
							}
						} else {
							//session有效
//							System.out.println("[RedisSessionVaidator]session有效："+user);
							boolean allow = true;
							if(secure.allowIdentitys() != null && secure.allowIdentitys().length > 0) {
								allow = false;
								for(String identity : secure.allowIdentitys()) {
									if(identity.equalsIgnoreCase(user.getUserType())) {
										allow = true;
										break;
									}
								}
								if(!allow) {
									//检查默认的全局允许身份
									Iterator<String> it = defaultAllowIdentitys.iterator();
									while (it.hasNext()) {
										String identity = (String) it.next();
										if(identity.equalsIgnoreCase(user.getUserType())) {
											allow = true;
											break;
										}
									}
								}
							}
							if(!allow) {
								result.setCode(APIConstants.IDENTITY_NOTPASS);
								result.setMessage("不允许此用户身份操作");
							} else {
								//允许访问，刷新session时间
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
					}
				} else if(aes != null){
					boolean validresult = validateRPCofNoUser(request);
					if(!validresult) {
						if(secure.redirectLogin()) {
							result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
						} else {
							result.setCode(APIConstants.ACCESS_NO_USER);
						}
						//会话失效2
						result.setMessage("请重新登录");
					}
				} else {
					if(secure.redirectLogin()) {
						result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
					} else {
						result.setCode(APIConstants.ACCESS_NO_USER);
					}
					result.setMessage("会话失效");
				}
			} else if(aes != null) {
				boolean validresult = validateRPCofNoUser(request);
				if(!validresult) {
					if(secure.redirectLogin()) {
						result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
					} else {
						result.setCode(APIConstants.ACCESS_NO_USER);
					}
					result.setMessage("请登录后操作2");
				}
			} else {
				if(secure.redirectLogin()) {
					result.setCode(APIConstants.ACCOUNT_NO_LOGIN);
				} else {
					result.setCode(APIConstants.ACCESS_NO_USER);
				}
				result.setMessage("请登录后操作");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setCode(APIConstants.CODE_SERVER_ERR);
			result.setMessage("session validate fail");
		}
		
		return result;
	}

	/**
	 * 后台定时任务无登录用户的请求验证方式
	 * @param request
	 * @param result
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws DecoderException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private boolean validateRPCofNoUser(HttpServletRequest request) {
		String authHead = request.getHeader(Authorization);
		if(StringUtils.isNotEmpty(authHead)) {
			try {
				authHead = authHead.replaceFirst("Bearer ", "");
				String userJson = aes.decryptBase64(authHead);
				UserVO user = JacksonTools.getObjectMapper().readValue(userJson, UserVO.class);
				SsoContext.setUser(user);
				SsoContext.setUserId(user.getUserId());
				SsoContext.setAccount(user.getUserAccount());
				SsoContext.setToken(authHead);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
