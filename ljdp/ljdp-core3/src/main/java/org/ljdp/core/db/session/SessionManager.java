package org.ljdp.core.db.session;

import org.ljdp.core.dao.DAO;
import org.ljdp.core.dao.GeneralDAO;

public interface SessionManager {
	@SuppressWarnings("rawtypes")
	public DAO newDAO();
	public GeneralDAO newGeneralDAO();
	
	public SessionFactoryRouter getSessionFactoryRouter();
	
	public Object getSession();
	
	public void closeSession(Object session);
}
