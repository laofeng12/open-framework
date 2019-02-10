package org.ljdp.component.user;

import java.io.Serializable;
import java.util.List;

public class DBAccessUser implements Serializable {
	private static final long serialVersionUID = -2285986510278800993L;
	
	private String id;
	private String IP;
	private String accessDB;
	private String account;
	private String name;
	private List<String> roles;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private static DBAccessUser innerUser;
	
	static {
		innerUser = new DBAccessUser();
		innerUser.setId("ROOT");
		innerUser.setIP("127.0.0.1");
		innerUser.setAccessDB("DEFAULT");
	}

	public String getId() {
		return id;
	}

	public void setId(String operatorCode) {
		this.id = operatorCode;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
		IP = ip;
	}

	public static DBAccessUser getInnerUser() {
		return innerUser;
	}

	public static void setInnerUser(DBAccessUser innerUser) {
		DBAccessUser.innerUser = innerUser;
	}

	public String getAccessDB() {
		return accessDB;
	}

	public void setAccessDB(String accessDB) {
		this.accessDB = accessDB;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
