package org.ljdp.core.spring.dds;

import javax.sql.DataSource;

public class DataSourceWrapper {

	private DataSource dataSource;

	public DataSourceWrapper(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
