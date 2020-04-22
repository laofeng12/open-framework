package org.ljdp.log.aop;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.config.Env;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.bean.TubTools;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.log.annotation.LogConfig;
import org.ljdp.log.model.RequestLog;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ControllerLogAspect {
	
	public static int Service_Max_Capacity = 5000;
	public static final BlockingQueue<RequestLog> queue;
	public static Class<?> logCls;
	private Logger log = LoggerFactory.getLogger("API-Access");
	
	public static Map<String,LogConfig> annomap = new HashMap<String,LogConfig>();
	
	private SequenceService mySequenceService;
	private String sequenceServiceName;
	private boolean defaultPrintLog = true;
	
	static {
		String logcfg = Env.getCurrent().getConfigFile().getValue("request.log.entity");
		if(logcfg != null) {
			System.out.println("request.log.entity:"+logcfg);
			try {
				logCls = Class.forName(logcfg);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("request.log.entity: null");
		}
		String tmp_max = Env.getCurrent().getConfigFile().getValue("service_max_capacity");
		if(StringUtils.isNotEmpty(tmp_max)) {
			try {
				Service_Max_Capacity = Integer.parseInt(tmp_max);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("service_max_capacity:"+Service_Max_Capacity);
		queue = new LinkedBlockingQueue<>(Service_Max_Capacity);
	}
	
	public ControllerLogAspect() {
		
	}
	
	public ControllerLogAspect(SequenceService mySequenceService) {
		this.mySequenceService = mySequenceService;
	}

	public Object doAudit(ProceedingJoinPoint point) throws Throwable
	{
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
		
		if(logCls == null) {
			return point.proceed();
		}
		Object returnVal = null;
		Class<?> bodyClass = null;
		try {
			
			String tub = request.getParameter("tub");
			if(StringUtils.isNotEmpty(tub)) {
				SsoContext.setTub(tub);
			}

			Object[] args = point.getArgs();
//			String methodName = point.getSignature().getName();
			//类
			Class<?> targetClass = point.getSignature().getDeclaringType();
			//方法
//			Method[] methods = targetClass.getMethods();
//			Method method = null;
//			for (int i = 0; i < methods.length; i++){
//				if (methods[i].getName().equals(methodName)){
//					if(methods[i].getParameterTypes().length == args.length) {
//						method = methods[i];
//					}
//					break;
//				}
//			}
//			if (method == null)
//				return point.proceed();
			Method method = ((MethodSignature)point.getSignature()).getMethod();
			

			String iden = targetClass.getSimpleName() + "." + method.getName();
			
			if(!annomap.containsKey(iden)){
				Method m = ((MethodSignature)point.getSignature()).getMethod();
				for(Annotation ano : m.getAnnotations()){
					if(ano.annotationType().equals(LogConfig.class)){
						 annomap.put(iden, (LogConfig) ano);
						 break;
					 }
				}
				if(!annomap.containsKey(iden)){
					annomap.put(iden, null);
				}
			}
			LogConfig logCfg = annomap.get(iden);
			
			String clientIp = getClientIpAddr(request);
			String localIp = request.getLocalAddr();
			String fromChannel = request.getParameter("fromChannel");
			String fromChannel2 = request.getParameter("fromChannel2");
			String fromHost = request.getHeader("X-Host");
			if(fromHost == null) {
				fromHost = request.getHeader("Host");
			}
//			String loginId = request.getParameter("loginId");
			String tokenid = request.getHeader("authority-token");
			if(StringUtils.isEmpty(tokenid)) {
				tokenid = request.getHeader("Authorization");
			}
			if(StringUtils.isEmpty(tokenid)) {
				tokenid = request.getParameter("tokenid");
			}
			String userAgent = request.getParameter("userAgent");
			if(StringUtils.isEmpty(userAgent)){
				userAgent = request.getHeader("user-agent");
			}
			String fromapp = request.getParameter("fromapp");
			String httpMethod = request.getMethod();
			if("GET".equals(httpMethod)) {
				if(StringUtils.isNotEmpty(userAgent)) {
					userAgent = URLDecoder.decode(userAgent, "utf-8");
				}
			}
			
//			StringBuffer inContent = new StringBuffer(1000);
			Map<String, Object> inMap = new HashMap<>();
			Object bodyValue = null;
			String reqParams = "";
			if(logCfg == null || logCfg.saveParams()) {
				Parameter[] params = method.getParameters();
				boolean isRequestBody = false;
				for (int i = 0; i < params.length; i++) {
					Parameter p = params[i];
					if(args == null || args.length <= i) {
						break;
					}
					if(args[i] == null) {
						continue;
					}
					if(ServletRequest.class.isAssignableFrom(args[i].getClass())) {
						continue;
					}
					if(ServletResponse.class.isAssignableFrom(args[i].getClass())) {
						continue;
					}
					if(args[i].getClass().getName().startsWith("javax.")) {
						continue;
					}
					if(args[i].getClass().getName().startsWith("java.io.")) {
						continue;
					}
					if(args[i].getClass().getName().startsWith("org.apache.")) {
						continue;
					}
					if(args[i].getClass().getName().equals("org.springframework.web.multipart.MultipartFile")) {
						continue;
					}
					String paramName = null;
					RequestParam rp = p.getAnnotation(RequestParam.class);
					if(rp != null) {
						paramName = rp.value();
						if(StringUtils.isEmpty(paramName)) {
							paramName = rp.name();
						}
					}
					PathVariable pv = p.getAnnotation(PathVariable.class);
					if(pv != null) {
						paramName = pv.value();
					}
					RequestBody rbody = p.getAnnotation(RequestBody.class);
					if(rbody != null) {
						isRequestBody = true;
						bodyValue = args[i];
						bodyClass = p.getType();
						break;
					}
//					if(i > 0) {
//						inContent.append(",");
//					}
					
//					System.out.println("paramName="+paramName);
//					System.out.println("args="+args[i]);
					
					if(paramName == null) {
						Map<String, Object> argMap = MyBeanUtils.reflectionToMap(args[i]);
						inMap.putAll(argMap);
//						inContent.append(MyBeanUtils.reflectionToString(args[i]));
					} else {
//						inContent.append(paramName+"="+args[i]);
						inMap.put(paramName, args[i]);
					}
				}
				String[] desensitizeParam = {};
				if(logCfg != null && logCfg.desensitizeParams() != null) {
					desensitizeParam = logCfg.desensitizeParams();
				}
				if(isRequestBody) {
					boolean isList = false;
					if(bodyClass.equals(ArrayList.class) || bodyClass.equals(List.class)) {
						isList = true;
					} else {
						Class<?>[] clss = bodyClass.getInterfaces();
						for (Class<?> clz : clss) {
							if(clz.equals(List.class)) {
								isList = true;
								break;
							}
						}
					}
					if(isList) {
						reqParams = JacksonTools.getObjectMapper().writeValueAsString(bodyValue);
					} else {
						try {
							for (String ds : desensitizeParam) {
								PropertyUtils.setProperty(bodyValue, ds, "***");
							}
							//对象如果用了lombok后，用不了PropertyUtils这个工具
							reqParams = JacksonTools.getObjectMapper().writeValueAsString(bodyValue);
						} catch (JsonProcessingException e) {
//							System.out.println("解析参数失败："+e.getMessage());
							Object myvalue = bodyClass.newInstance();
							PropertyUtils.copyProperties(myvalue, bodyValue);
							for (String ds : desensitizeParam) {
								PropertyUtils.setProperty(myvalue, ds, "***");
							}
							reqParams = JacksonTools.getObjectMapper().writeValueAsString(myvalue);
						}
					}
				} else {
					Iterator<String> it = inMap.keySet().iterator();
					while (it.hasNext()) {
						String key = (String) it.next();
						//过滤密码字段
						if(key.equals("userPwd") || key.equals("pwd") || key.contains("password")) {
							inMap.put(key, "***");
						}
					}
					for (String ds : desensitizeParam) {
						if(inMap.containsKey(ds)) {
							inMap.put(ds, "***");
						}
					}
					reqParams = JacksonTools.getObjectMapper().writeValueAsString(inMap);
				}
			}
			SequenceService ss = ConcurrentSequence.getCentumInstance();
			if(mySequenceService != null) {
				ss = mySequenceService;
			} else {
				if(sequenceServiceName != null) {
					//查找自定义的序列
					mySequenceService = (SequenceService)SpringContextManager.getBean(sequenceServiceName);
					if(mySequenceService != null) {
						ss = mySequenceService;
						log.info("LogSequenceInit: use "+sequenceServiceName);
					} else {
						log.info("LogSequenceInit: can not find "+sequenceServiceName);
					}
				}
			}
			SsoContext.setRequestId(ss.getSequence());
			Date requestDate = new Date();
			returnVal = point.proceed();
			Date responseDate = new Date();
			
//			System.out.println(iden+" "+logreq.getRequestParams());
//			System.out.println(logreq.toString());
//			String reqParams = inContent.toString();
			
			if(null == logCfg) {
				//如果没有配置，那么看默认设置
				if(defaultPrintLog) {
					log.info(iden+" "+reqParams);
				}
			} else if(logCfg.print()) {
				log.info(iden+" "+reqParams);
			}
			if(logCfg == null || logCfg.save()) {
				String loginId = SsoContext.getAccount();
				Object user = SsoContext.getUser();
				if(user != null) {
					try {
						String userCode = (String)PropertyUtils.getProperty(user, "userCode");
						if(StringUtils.isNotEmpty(userCode)) {
							loginId = userCode;//优先用户编码
						}
						String platform = (String)PropertyUtils.getProperty(user, "platform");
						if(StringUtils.isNotEmpty(platform)) {
							fromChannel = platform;//使用的是哪个平台
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(loginId == null) {
					Long passId = SsoContext.getPassId();
					if(passId != null) {
						loginId = passId.toString();
					}
				}
				
				String[] theTubs = null;
				if(StringUtils.isNotEmpty(tub)) {
					if(tub.indexOf("|") > 0) {
						tub = tub.replaceAll("\\|", "-");//临时代码，为了兼容之前的
					}
					String[] tubs = tub.split(TubTools.SPLIT);
					String currentTub = tubs[tubs.length-1];
					theTubs = currentTub.split("\\.");
				}
				
				RequestLog logreq = (RequestLog)logCls.newInstance();
				if(theTubs != null) {
					logreq.setTub1(theTubs[0]);
					logreq.setTub2(theTubs[1]);
					logreq.setTub3(theTubs[2]);
				}
				
				logreq.setAccount(loginId);
				logreq.setClientIp(clientIp);
				logreq.setFromChannel(fromChannel);
				String ac = null;
				if(fromHost != null) {
					ac = fromHost;
				}
				if(fromChannel2 != null && !fromChannel2.equalsIgnoreCase("null")) {
					if(ac != null) {
						ac += "|";
					}
					ac += fromChannel2;
				}
				logreq.setAdvertChannel(ac);
				logreq.setFromapp(fromapp);
				logreq.setLocalIp(localIp);
				logreq.setRequestIden(iden);
				logreq.setRequestTime(requestDate);
				logreq.setResponseTime(responseDate);
				logreq.setRequestParams(reqParams);
				logreq.setTokenId(tokenid);
				logreq.setUserAgent(userAgent);
				ApiResponse apiresult = SsoContext.getApiResponse();
				if(apiresult != null) {
					if(apiresult.getCode() != null) {
						logreq.setResponseCode(apiresult.getCode().toString());
					}
					logreq.setResponseMessage(apiresult.getMessage());
				} else {
					logreq.setResponseCode("200");
				}
				Long logReqId = SsoContext.getRequestId();
				if(returnVal != null) {
					if(returnVal instanceof Serializable) {
						if(returnVal instanceof ApiResponse) {
							ApiResponse ar = (ApiResponse)returnVal;
							if(logReqId != null) {
								ar.setRequestId(logReqId.toString());//已经有请求ID了
							} else {
								if(StringUtils.isEmpty(ar.getRequestId())) {//没有ID
									logReqId = ss.getSequence();
									ar.setRequestId(logReqId.toString());
								} 
//							else if(StringUtils.isNumeric(ar.getRequestId())){//接口返回时已经设置了ID
//								logReqId = new Long(ar.getRequestId());
//							}
							}
							if(ar.getCode() == null) {
								ar.setCode(APIConstants.CODE_SUCCESS);
							}
						}
						String json = JacksonTools.getObjectMapper().writeValueAsString(returnVal);
						logreq.setResponseData(json);
					}
//					logreq.setResponseData(MyBeanUtils.reflectionToString(returnVal).toString());
				} else {
					ApiResponse result = SsoContext.getApiResponse();
					if(result != null) {
						if(logReqId != null) {
							result.setRequestId(logReqId.toString());
						} else {
							if(StringUtils.isEmpty(result.getRequestId())) {
								logReqId = ss.getSequence();
								result.setRequestId(logReqId.toString());
							}
//							else if(StringUtils.isNumeric(result.getRequestId())){
//								logReqId = new Long(result.getRequestId());
//							}
						}
//						logreq.setResponseData(MyBeanUtils.reflectionToString(result).toString());
						String json = JacksonTools.getObjectMapper().writeValueAsString(result);
						logreq.setResponseData(json);
					}
				}
				if(logReqId == null) {
					logReqId = ss.getSequence();
				}
				logreq.setId(logReqId);
				response.addHeader("requestId", logReqId.toString());
				queue.put(logreq);
			}
//			System.out.println("结束：ControllerLogAspect");
		} catch (Exception e) {
			log.error("bodyClass:"+bodyClass);
			e.printStackTrace();
		} finally {
			SsoContext.clearContext();//防止由于使用servlet的线程池导致的bug
		}
		return returnVal;
	}
	
	private static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
	}
	
	/**
	 * 是否达到服务器最大访问容量
	 * @return
	 */
	public static boolean isInMaxCapacity() {
		if(queue.size() >= Service_Max_Capacity) {
			return true;
		}
		return false;
	}

	public SequenceService getMySequenceService() {
		return mySequenceService;
	}

	public void setMySequenceService(SequenceService mySequenceService) {
		this.mySequenceService = mySequenceService;
		if(null == mySequenceService) {
			log.info("LogSequenceInit: mySequenceService is null");
		} else {
//			log.info("LogSequenceInit: "+mySequenceService);
		}
	}

	public String getSequenceServiceName() {
		return sequenceServiceName;
	}

	public void setSequenceServiceName(String sequenceServiceName) {
		this.sequenceServiceName = sequenceServiceName;
	}

	public boolean isDefaultPrintLog() {
		return defaultPrintLog;
	}

	public void setDefaultPrintLog(boolean defaultPrintLog) {
		this.defaultPrintLog = defaultPrintLog;
	}
}
