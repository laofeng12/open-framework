package org.ljdp.core.db.pool;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidProperty {
	private String url;
	private String username;
	private String password;
	private Integer initialSize;
	private Integer maxActive;
	private Integer minIdle;
	private Integer maxWait;
	private Boolean poolPreparedStatements;
	private Integer maxPoolPreparedStatementPerConnectionSize;
	private Integer maxOpenPreparedStatements;
	private String validationQuery;
	private Integer validationQueryTimeout;
	private Boolean testWhileIdle;
	private Boolean testOnBorrow;
	private Boolean testOnReturn;
	private String filters;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}

	public Boolean getPoolPreparedStatements() {
		return poolPreparedStatements;
	}

	public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public Integer getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public Integer getMaxOpenPreparedStatements() {
		return maxOpenPreparedStatements;
	}

	public void setMaxOpenPreparedStatements(Integer maxOpenPreparedStatements) {
		this.maxOpenPreparedStatements = maxOpenPreparedStatements;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public Integer getValidationQueryTimeout() {
		return validationQueryTimeout;
	}

	public void setValidationQueryTimeout(Integer validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}
	

	public void initConfig(DruidDataSource ds) throws SQLException {
		ds.setUsername(this.getUsername());
		ds.setPassword(this.getPassword());
        ds.setUrl(this.getUrl());
        ds.setInitialSize(this.getInitialSize());
        ds.setMaxActive(this.getMaxActive());
        ds.setMinIdle(this.getMinIdle());
        ds.setMaxWait(this.getMaxWait());
        ds.setPoolPreparedStatements(this.getPoolPreparedStatements());
        ds.setMaxPoolPreparedStatementPerConnectionSize(this.getMaxPoolPreparedStatementPerConnectionSize());
        ds.setMaxOpenPreparedStatements(this.getMaxOpenPreparedStatements());
        ds.setValidationQuery(this.getValidationQuery());
        ds.setValidationQueryTimeout(this.getValidationQueryTimeout());
        ds.setTestOnBorrow(this.getTestOnBorrow());
        ds.setTestOnReturn(this.getTestOnReturn());
        ds.setTestWhileIdle(this.getTestWhileIdle());
        ds.setFilters(this.getFilters());
	}

}
