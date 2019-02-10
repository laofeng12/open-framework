package org.ljdp.support.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContext;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.core.service.GeneralService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.log.aop.ControllerLogAspect;
import org.ljdp.log.model.RequestErrorLog;
import org.ljdp.log.model.RequestLog;
import org.ljdp.secure.aop.ExceptionAspect;

/**
 * 批量保存日志到数据库
 * @author hzy
 *
 */
public class ApiLogSaveRunner implements Runnable {

	public static boolean running = true;
	private static short circualCount = 0;
	private static short circualErrCount = 0;
	private static int Interval = 1000;//下次触发保存日志间隔
	
	private GeneralService service;
	private RequestLogProcesser processer;
	private Boolean needSave = true;
//	private Boolean needPrint = false;
	
	public ApiLogSaveRunner() {
		
	}
	
	@Override
	public void run() {
		System.out.println("[ApiLogSaveRunner]启动日志批量保存线程...");
		try {
			Thread.sleep(10000);
			while(true) {
				if(SpringContext.mySpringContext != null) {
					service = ServiceFactory.buildGeneral();
					System.out.println("[ApiLogSaveRunner]初始化完成");
					break;
				}
				System.out.println("[ApiLogSaveRunner]LJDP还未初始化完成，继续等待...");
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR][ApiLogSaveRunner]初始化失败!!!!!!!!");
			return;
		}
		
		ConfigFile mycfg = Env.getCurrent().getConfigFile();
    	String procesId = mycfg.getValue("request.log.processer");
    	if(StringUtils.isNotBlank(procesId)) {
    		System.out.println("[ApiLogSaveRunner]load: "+procesId);
    		processer = (RequestLogProcesser)SpringContextManager.getBean(procesId);
    		System.out.println("[ApiLogSaveRunner]processer="+processer);
    	}
    	String saveLog = mycfg.getValue("request.savelog");
    	if(StringUtils.isNotBlank(saveLog)) {
    		System.out.println("[ApiLogSaveRunner]request.savelog: "+saveLog);
    		try {
    			needSave = new Boolean(saveLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	/*String printLog = mycfg.getValue("request.printlog");
    	if(StringUtils.isNotBlank(printLog)) {
    		System.out.println("[ApiLogSaveRunner]request.printlog: "+printLog);
    		try {
    			needPrint = new Boolean(printLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}*/
		while(running) {
			try {
				Thread.sleep(Interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			doBatchSaveApiLog();
			
			doBatchSaveErrorLog();
		}
	}

	private void doBatchSaveErrorLog() {
		BlockingQueue<RequestErrorLog> queue = ExceptionAspect.queue;
		int size = queue.size();
		if(size > 0) {
			circualErrCount++;
			if(size < 100 && circualErrCount < 10) {//如果日志量太少或者进来小于10次，那么等下次再保存
				return;
			}
			circualErrCount = 0;
			try {
				List<Serializable> logList = new ArrayList<>(size);
				for(int i=0; i < size; i++) {
					RequestErrorLog r = queue.take();
					logList.add(r);
				}
				if(needSave) {
					service.doBatchCreate(logList);
					System.out.println("Save Error Log:"+logList.size());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void doBatchSaveApiLog() {
		BlockingQueue<RequestLog> queue = ControllerLogAspect.queue;
		int size = queue.size();
		if(size > 0) {
			circualCount++;
			if(size < 100 && circualCount < 10) {//如果日志量太少或者进来小于10次，那么等下次再保存
				return;
			}
			circualCount = 0;
			int processNum = 0;
			while(size > 0) {
				List<Serializable> logList = new ArrayList<>(1000);
				List<RequestLog> logList2 = new ArrayList<>(1000);
				try {
					for(int i=0; i < size && i < 1000; i++) {
						RequestLog r = queue.take();
						logList.add(r);
						logList2.add(r);
					}
					if(needSave) {
						service.doBatchCreate(logList);
						System.out.println("Save Log:"+logList.size());
					}
					/*if(needPrint) {
						for (RequestLog r : logList2) {
							String df = DateFormater.formatDatetime(r.getRequestTime());
							System.out.println(df+" ["+r.getRequestIden()+"]"+r.getRequestParams());
						}
					}*/
					if(processer != null) {
						processer.doBatch(logList2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				size -= logList.size();
				processNum += logList.size();
			}
			if(processNum > 1000) {
				if(needSave) {
					System.out.println("Save Log Total:"+processNum);
				}
			}
			if(processNum >= (ControllerLogAspect.Service_Max_Capacity*0.5)) {
				Interval = 0;
			} else {
				Interval = 1000;
			}
		}
	}

	public RequestLogProcesser getProcesser() {
		return processer;
	}

	public void setProcesser(RequestLogProcesser processer) {
		this.processer = processer;
	}

}
