package org.ljdp.secure.aop;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.common.config.Env;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.exception.BusinessException;
import org.ljdp.component.exception.MServiceCallException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.log.aop.ControllerLogAspect;
import org.ljdp.log.model.RequestErrorLog;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ExceptionAspect {
	
	public static final BlockingQueue<RequestErrorLog> queue;
	public static Class logCls;
	public int resultPosition = APIConstants.HTTP_POSITION_BODY;//Body: code和message存放在报文区域。Head：code和message存放在http head，code共用http status
	
	static {
		String logcfg = Env.getCurrent().getConfigFile().getValue("request.errorlog.entity");
		if(logcfg != null) {
			System.out.println("request.errorlog.entity:"+logcfg);
			try {
				logCls = Class.forName(logcfg);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		queue = new LinkedBlockingQueue<>(ControllerLogAspect.Service_Max_Capacity);
	}
	
	public Object doException(ProceedingJoinPoint point) throws Throwable {
		try {
			if(ControllerLogAspect.isInMaxCapacity()) {
				throw new BusinessException(APIConstants.CODE_SERVICE_BUSY, "抱歉，服务繁忙，请稍后再试");
			}
			return point.proceed();
		} catch (Throwable e) {
			return exceptionProcess(point, e);
		} finally {
			
//			System.out.println("结束：ExceptionAspect");
		}
	}
	private Object exceptionProcess(ProceedingJoinPoint jp, Throwable exp) {
		Method methodsrc = ((MethodSignature)jp.getSignature()).getMethod();
		String iden = jp.getTarget().getClass().getSimpleName() + "." + methodsrc.getName();
		Class retCls = methodsrc.getReturnType();
		
		if(exp instanceof APIException){
			APIException e = (APIException) exp;
			//打印错误日志
			System.out.println("APIException["+iden+"]"+e.getCode()+":"+e.getMessage());
			
			return getReturnObject(retCls, e, null, null);
		} else if(exp instanceof MServiceCallException){
			MServiceCallException ce = (MServiceCallException) exp;
			//打印错误日志
			System.out.println("MServiceCallException["+iden+"]"+ce.getCode()+":"+ce.getMessage());
			
			return getReturnObject(retCls, null, null, ce);
			
			
		} else if(exp instanceof BusinessException){
			BusinessException e = (BusinessException) exp;
			//打印错误日志
			System.out.println("["+iden+"]"+e.getCode()+":"+e.getMessage());
			
			return getReturnObject(retCls, null, e, null);
		} else {
			System.out.println("[Error]["+iden+"]:"+exp.getMessage());
			exp.printStackTrace();
			if(logCls != null) {//保存错误日志
				try {
					SequenceService cs = ConcurrentSequence.getCentumInstance();
					RequestErrorLog log = (RequestErrorLog)logCls.newInstance();
					log.setErrorId(cs.getSequence(""));
					
					Long reqId = cs.getSequence();
					log.setRequestId(reqId.toString());
					SsoContext.setRequestId(reqId);
					
					StringWriter sw = new StringWriter();
					exp.printStackTrace(new PrintWriter(sw, true));
					String error = sw.toString();
					log.setError(error);
					log.setErrorDate(new Date());
					queue.put(log);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return getReturnObject(retCls, null, null, null);
		}
	}
	private Object getReturnObject(Class retCls, APIException ae, BusinessException be, MServiceCallException ce) {
		boolean isApiRet = false;
		boolean isApiImplRet = false;
		boolean isVoidRet = false;
//		boolean isRespEntity = false;
		boolean isCustomRet = false;//返回对象没有实现ApiResponse接口，是自定义对象
		ApiResponse retResp = null;
		Object retObj = null;
//		System.out.println("[retCls]"+retCls);
		if(retCls.equals(ApiResponse.class) || retCls.equals(BasicApiResponse.class)) {
			isApiRet = true;
		} else if(retCls.equals(Void.class) || retCls.toString().equals("void")) {
			isVoidRet = true;
		} else if(retCls.equals(ResponseEntity.class)) {
//			isRespEntity = true;
			ApiResponse resp = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, "服务异常");
			if(ae != null) {
				resp = new BasicApiResponse(ae.getCode(), ae.getMessage());
			} else if(be != null) {
				resp = new BasicApiResponse(be.getCode(), be.getMessage());
			} else if(ce != null) {
				resp = new BasicApiResponse(ce.getCode(), ce.getMessage());
			}
			ResponseEntity entity;
			if(resp.getCode().intValue() == APIConstants.ACCOUNT_NO_LOGIN) {
				entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
			} else if(resp.getCode().intValue() == APIConstants.CODE_AUTH_FAILD) {
				entity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
			} else if(resp.getCode().intValue() == APIConstants.ACCESS_NO_USER) {
				entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
			} else {
				entity = ResponseEntity.badRequest().body(resp);
			}
			
			retObj = entity;
		} else {
			isApiImplRet = hasInterface(retCls, ApiResponse.class);
			try {
				if(isApiImplRet) {
					if(retCls.isInterface()) {
						String clsImpl = retCls.getName()+"Impl";
						System.out.println("[ExceptionAspect]find return class:"+clsImpl);
						retResp = (ApiResponse)Class.forName(clsImpl).newInstance();
					} else {
						retResp = (ApiResponse)retCls.newInstance();
					}
				}  else if(retCls.isInterface()) {
					if(retCls.equals(Map.class)) {
						HashMap<String, Object> m = new HashMap<>();
						if(ae != null) {
							m.put("code", ae.getCode());
							m.put("message", ae.getMessage());
						} else if(be != null) {
							m.put("code", be.getCode());
							m.put("message", be.getMessage());
						} else if(ce != null) {
							m.put("code", ce.getCode());
							m.put("message", ce.getMessage());
						}
						retObj = m;
					} else if(retCls.equals(List.class)) {
						ArrayList<Object> al = new ArrayList<>();
						if(ae != null) {
							al.add(ae);
						} else if(be != null) {
							al.add(be);
						} else if(ce != null) {
							al.add(ce);
						}
						retObj = al;
					} else if(retCls.equals(Set.class)) {
						HashSet<Object> s = new HashSet<>();
						if(ae != null) {
							s.add(ae.getCode());
							s.add(ae.getMessage());
						} else if(be != null) {
							s.add(be.getCode());
							s.add(be.getMessage());
						} else if(ce != null) {
							s.add(ce.getCode());
							s.add(ce.getMessage());
						}
						retObj = s;
					}
				} else if(retCls.equals(String.class)) {
					if(ae != null) {
						retObj = ae.getMessage();
					} else if(be != null) {
						retObj = be.getMessage();
					} else if(ce != null) {
						retObj = ce.getMessage();
					}
				} else {
//					retObj = retCls.newInstance();
					isCustomRet = true;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
//		System.out.println("[isApiRet]="+isApiRet);
//		System.out.println("[isApiImplRet]="+isApiImplRet);
//		System.out.println("[isVoidRet]="+isVoidRet);
//		System.out.println("[isRespEntity]="+isRespEntity);
		if(APIConstants.HTTP_POSITION_HEAD == resultPosition) {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
			//返回错误信息需要放到head
			if(ae != null) {
				response.setStatus(ae.getCode());
//				response.addHeader("message", ae.getMessage());
			} else if(be != null) {
				response.setStatus(be.getCode());
//				response.addHeader("message", be.getMessage());
			} else if(ce != null) {
				response.setStatus(ce.getCode());
//				response.addHeader("message", ce.getMessage());
			} else {
				response.setStatus(APIConstants.CODE_SERVER_ERR);
//				response.addHeader("message", "服务异常");
			}
		}
		{
			//存入线程区，下个环节日志保存可以用到
			ApiResponse resp = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, "服务异常");
			if(ae != null) {
				resp = new BasicApiResponse(ae.getCode(), ae.getMessage());
			} else if(be != null) {
				resp = new BasicApiResponse(be.getCode(), be.getMessage());
			} else if(ce != null) {
				resp = new BasicApiResponse(ce.getCode(), ce.getMessage());
			}
			SsoContext.setApiResponse(resp);
		}
		if(isApiRet) {
			BasicApiResponse resp = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, "服务异常");
			if(ae != null) {
				resp = new BasicApiResponse(ae.getCode(), ae.getMessage());
			} else if(be != null) {
				resp = new BasicApiResponse(be.getCode(), be.getMessage());
			} else if(ce != null) {
				resp = new BasicApiResponse(ce.getCode(), ce.getMessage());
			}
			return resp;
		} else if(isApiImplRet) {
			if(retResp != null) {
				if(ae != null) {
					retResp.setCode(ae.getCode());
					retResp.setMessage(ae.getMessage());
				} else if(be != null) {
					retResp.setCode(be.getCode());
					retResp.setMessage(be.getMessage());
				} else {
					retResp.setCode(APIConstants.CODE_SERVER_ERR);
					retResp.setMessage("服务异常");
				}
				return retResp;
			}
		} else if(isVoidRet) {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
			BasicApiResponse resp = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, "服务异常");
			if(ae != null) {
				resp = new BasicApiResponse(ae.getCode(), ae.getMessage());
			} else if(be != null) {
				resp = new BasicApiResponse(be.getCode(), be.getMessage());
			} else if(ce != null) {
				resp = new BasicApiResponse(ce.getCode(), ce.getMessage());
			}
			try {
				JacksonTools.writePage(resp, response);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(isCustomRet) {
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
			HttpServletResponse response = ((ServletRequestAttributes)ra).getResponse();
			BasicApiResponse resp = new BasicApiResponse(APIConstants.CODE_SERVER_ERR, "服务异常");
			if(ae != null) {
				resp = new BasicApiResponse(ae.getCode(), ae.getMessage());
			} else if(be != null) {
				resp = new BasicApiResponse(be.getCode(), be.getMessage());
			} else if(ce != null) {
				resp = new BasicApiResponse(ce.getCode(), ce.getMessage());
			}
			try {
				if(resp.getCode().intValue() == APIConstants.ACCOUNT_NO_LOGIN) {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
				} else if(resp.getCode().intValue() == APIConstants.CODE_AUTH_FAILD) {
					response.setStatus(HttpStatus.FORBIDDEN.value());
				} else if(resp.getCode().intValue() == APIConstants.ACCESS_NO_USER) {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
				} else {
					response.setStatus(HttpStatus.BAD_REQUEST.value());
				}
				JacksonTools.writePage(resp, response);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retObj;
	}
	
	public boolean hasInterface(Class myClass, Class intfCls) {
		if(myClass == null) {
			return false;
		}
		Class[] intfs = myClass.getInterfaces();
		for (Class cls : intfs) {
//			System.out.println("[retCls interfaces]"+cls);
			if(cls.equals(intfCls)) {
				return true;
			}
		}
		return hasInterface(myClass.getSuperclass(), intfCls);
	}
	public int getResultPosition() {
		return resultPosition;
	}
	public void setResultPosition(int resultPosition) {
		this.resultPosition = resultPosition;
	}
	
}
