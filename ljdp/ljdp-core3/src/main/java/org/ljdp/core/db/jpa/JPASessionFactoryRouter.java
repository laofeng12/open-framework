package org.ljdp.core.db.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ljdp.core.db.session.SessionFactoryRouter;
import org.ljdp.core.db.session.SessionManager;
import org.springframework.stereotype.Repository;

@Repository
public class JPASessionFactoryRouter implements SessionFactoryRouter {

	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public SessionManager getSessionManager() {
		JPASessionManager sm = new JPASessionManager();
		sm.setEm(em);
		sm.setSessionFactoryRouter(this);
		return sm;
	}

	@SuppressWarnings("rawtypes")
	public SessionManager getSessionManager(Class voClass) {
		return getSessionManager();
	}

	public SessionManager getSessionManager(String db) {
		return getSessionManager();
	}

	public String getDefaultDB() {
		return DB_DEFAULT;
	}

}
