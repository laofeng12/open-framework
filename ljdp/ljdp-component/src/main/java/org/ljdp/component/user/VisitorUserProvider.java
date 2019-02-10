package org.ljdp.component.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class VisitorUserProvider implements UserProvider {

	public DBAccessUser getUser() {
		DBAccessUser user = new DBAccessUser();
		user.setId("VISITOR");
		user.setName("游客");
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			user.setIP(ia.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return user;
	}

}
