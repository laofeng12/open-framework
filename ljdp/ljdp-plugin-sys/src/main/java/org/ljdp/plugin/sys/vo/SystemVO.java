package org.ljdp.plugin.sys.vo;

import java.io.Serializable;

public class SystemVO  implements Serializable{
	private static final long serialVersionUID = -131745598652582734L;
	private String sysId;
	private String sysName;
	
	public SystemVO() {
		
	}
	
	public SystemVO(String sysId, String sysName) {
		super();
		this.sysId = sysId;
		this.sysName = sysName;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
}
