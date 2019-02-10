package org.ljdp.core.db.session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.core.db.jpa.JPASessionFactoryRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SessionUtil {
	private static Logger log = LoggerFactory.getLogger(SessionUtil.class);
    
    private static boolean autoSessionClose = true;
    
    private static final ThreadLocal<ThreadSession> threadPackage = new ThreadLocal<ThreadSession>();
    
    public static SessionManager getSessionManager() {
    	JPASessionFactoryRouter router = (JPASessionFactoryRouter)SpringContextManager.getBean("SessionFactoryRouter");
		return router.getSessionManager();
	}
    
    public static EntityManager openSession() {
        ThreadSession threadSession = (ThreadSession) threadPackage.get();
        if ( threadSession == null ) {
            threadSession = new ThreadSession();
            threadPackage.set(threadSession);
        }
        if ( threadSession.getSession() == null
                || !threadSession.getSession().isOpen() ) {
        	EntityManager session = (EntityManager)getSessionManager().getSession();
            threadSession.setSession(session);
            threadSession.count = 0;
            log.debug("Open Hibernate Session");
        }
        ++threadSession.count;
        return threadSession.getSession();
    }
    
    public static EntityManager getSession() {
    	ThreadSession threadSession = (ThreadSession) threadPackage.get();
        if ( threadSession == null ||  threadSession.getSession() == null ) {
            throw new NullPointerException("Thread Session is NULL");
        }
        return threadSession.getSession();
    }
    
    public static void closeSession() {
        ThreadSession threadSession = (ThreadSession) threadPackage.get();
        if ( threadSession != null ) {
            if ( threadSession.getSession() != null ) {
                if (threadSession.count <= 1 && isAutoSessionClose()) {
                    threadSession.getSession().close();
                    threadSession.setSession(null);
                    threadSession.setTrans(null);
                    log.debug("Close Hibernate Session");
                } else if ( threadSession.count > 0 ){
                    --threadSession.count;
                }
            }
        }
    }
    
    public static EntityTransaction beginTransaction() {
        ThreadSession threadSession = (ThreadSession)threadPackage.get();
        if (threadSession == null || threadSession.getSession() == null || !threadSession.getSession().isOpen()) {
            throw new NullPointerException("session is null or is not open");
        }
        EntityTransaction tran = threadSession.getTrans();
        if (tran == null) {
            tran = threadSession.getSession().getTransaction();
            tran.begin();
            threadSession.setTrans(tran);
            log.debug("Begin Transaction");
        } else if(!tran.isActive()) {
        	tran.begin();
            log.debug("Begin Transaction");
        }
        return tran;
    }
    
    public static void commitTransaction() {
        ThreadSession threadSession = (ThreadSession)threadPackage.get();
        if (threadSession == null) {
            throw new NullPointerException("session is null or is not open");
        }
        EntityTransaction tran = threadSession.getTrans();
        if (tran == null) {
            throw new RuntimeException("事务未开始，不能提交");
        }
        if (threadSession.count == 1) {
            tran.commit();
            threadSession.setTrans(null);
            log.debug("Commit Transaction");
        }
    }
    
    public static void rollbackTransaction() {
        ThreadSession threadSession = (ThreadSession)threadPackage.get();
        if (threadSession != null) {
        	EntityTransaction tran = threadSession.getTrans();
            if (tran != null) {
                if (threadSession.count == 1) {
                    tran.rollback();
                    log.debug("Rollback Transaction");
                }
            }
        }
    }
    
    public static void deleteSession() {
        ThreadSession threadSession = (ThreadSession) threadPackage.get();
        if ( threadSession != null ) {
            if ( threadSession.getSession() != null ) {
                threadSession.getSession().close();
                log.debug("Close Hibernate Session");
            }
            threadPackage.set(null);
        }
    }

	public static boolean isAutoSessionClose() {
		return autoSessionClose;
	}

	public static void setAutoSessionClose(boolean autoSessionClose) {
		SessionUtil.autoSessionClose = autoSessionClose;
	}
    
}
