package org.ljdp.core.db.jpa;

import javax.persistence.EntityManager;

import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.GeneralDAO;
import org.ljdp.core.db.session.SessionFactoryRouter;
import org.ljdp.core.db.session.SessionManager;

public class JPASessionManager implements SessionManager {
	private EntityManager em;
	private JPASessionFactoryRouter router;
	
	@SuppressWarnings("rawtypes")
	public DAO newDAO() {
		DAO dao = new JPABaseDAO();
		dao.setSessionManager(this);
		return dao;
	}
	
	public GeneralDAO newGeneralDAO() {
		GeneralDAO dao = new JPAGeneralDAO();
		dao.setSessionManager(this);
		return dao;
	}

	public SessionFactoryRouter getSessionFactoryRouter() {
		return router;
	}
	
	public void setSessionFactoryRouter(JPASessionFactoryRouter router) {
		this.router = router;
	}

	public Object getSession() {
		return em;
	}

	public void closeSession(Object session) {
		
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
