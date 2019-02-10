package org.ljdp.component.session;

import java.io.Serializable;

public class ReqParameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3771799695090855956L;
	private String name;
	private String value;
	
	public ReqParameter() {
		
	}
	
	public ReqParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
