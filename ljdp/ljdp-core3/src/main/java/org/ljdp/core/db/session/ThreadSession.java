package org.ljdp.core.db.session;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ThreadSession {
	private EntityManager session;

	private EntityTransaction trans;

	public int count;
	
	public boolean useTransaction;

	public EntityManager getSession() {
		return session;
	}

	public void setSession(EntityManager session) {
		this.session = session;
	}

	public EntityTransaction getTrans() {
		return trans;
	}

	public void setTrans(EntityTransaction trans) {
		this.trans = trans;
	}

	public boolean isUseTransaction() {
		return useTransaction;
	}

	public void setUseTransaction(boolean useTransaction) {
		this.useTransaction = useTransaction;
	}
}
