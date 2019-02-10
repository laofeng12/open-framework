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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.ljdp.common.config.Env;
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

public class ExceptionAspect {
	
	public static final BlockingQueue<RequestErrorLog> queue;
	public static Class logCls;
	
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
		ApiResponse retResp = null;
		Object retObj = null;
//		System.out.println("[retCls]"+retCls);
		if(retCls.equals(ApiResponse.class) || retCls.equals(BasicApiResponse.class)) {
			isApiRet = true;
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
					} else if(retCls.equals(String.class)) {
						if(ae != null) {
							retObj = ae.getMessage();
						} else if(be != null) {
							retObj = be.getMessage();
						} else if(ce != null) {
							retObj = ce.getMessage();
						}
					}
				} else if(retCls.equals(Void.class)) {
					
				} else {
					retObj = retCls.newInstance();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
//		System.out.println("[isApiRet]="+isApiRet);
//		System.out.println("[isApiImplRet]="+isApiImplRet);
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
		} else {
			return retObj;
		}
		return null;
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
}
