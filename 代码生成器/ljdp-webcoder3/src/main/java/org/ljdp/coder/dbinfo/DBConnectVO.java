package org.ljdp.coder.dbinfo;

public class DBConnectVO {

	private String driver;
	private String url;
	private String user;
	private String password;
	private String schema;
	private String dbType;
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	@Override
	public String toString() {
		return "DBConnectVO [driver=" + driver + ", url=" + url + ", user=" + user
				+ ", schema=" + schema + ", dbType=" + dbType + "]";
	}
}
