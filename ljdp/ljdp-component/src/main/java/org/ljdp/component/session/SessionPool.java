package org.ljdp.component.session;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionPool {
	private static Logger log = LoggerFactory.getLogger(SessionPool.class);
	private static Hashtable<String, Session> sessionTable = new Hashtable<String, Session>();
	private static long sessionLiveTime = 5 * 60 * 60L;//Session生存时间,单位秒
	private static long lastClearSessionTime;//最近一次清理过期Session时间（亳秒）
	
	private SessionPool() {
		
	}
	
	static {
		InputStream input = SessionPool.class.getResourceAsStream("/application.properties");
		Properties pro = new Properties();
		try {
			pro.load(input);
			input.close();
			String per = pro.getProperty("ljdp.session.timeout.minute");
			if(StringUtils.isNotBlank(per)) {
				int m = Integer.parseInt(per);
				sessionLiveTime = m * 60L;
				log.info("初始化session生存周期 = " + sessionLiveTime + "秒");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized Session createSession(String id) {
		Session session = new Session(id);
		session.setMaxInactiveInterval(sessionLiveTime);
		sessionTable.put(id, session);
		clearIfNeed();
		log.info("create user session: " + session.toString());
		return session;
	}
	
	public static synchronized Session getSession(String id) throws NullSessionException,
			SessionOverdueException {
		if(null == id) {
			throw new NullSessionException("session is null");
		}
		if(!sessionTable.containsKey(id)) {
			throw new NullSessionException("Not exist session id="+id);
		}
		Session session = (Session)sessionTable.get(id);
		if(session.isOverdue()) {
			removeSession(id);
			throw new SessionOverdueException("Sessin is overdue");
		}
		session.updateConnectTime();
		return session;
	}
	
	public static synchronized void removeSession(String id) {
		sessionTable.remove(id);
//		System.out.println("WS.SessionManager.removeSession sessionid="+id);
		log.info("removeSession sessionid="+id);
	}
	
	public static synchronized void clear() {
		Iterator<Session> it = sessionTable.values().iterator();
		ArrayList<String> odlist = new ArrayList<String>();
		while(it.hasNext()) {
			Session session = (Session)it.next();
			if(session.isOverdue()) {
				odlist.add(session.getId());
			}
		}
		for(int i = 0; i < odlist.size(); i++) {
			removeSession((String)odlist.get(i));
		}
		lastClearSessionTime = System.currentTimeMillis();
	}
	
	public static synchronized void clearIfNeed() {
		long interval = System.currentTimeMillis() - lastClearSessionTime;
		long intval_sec = interval / 1000;
		if(intval_sec > sessionLiveTime) {
			clear();
		}
	}

}
