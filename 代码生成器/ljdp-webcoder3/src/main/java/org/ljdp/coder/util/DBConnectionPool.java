package org.ljdp.coder.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.*;

import javax.sql.DataSource;

import org.ljdp.coder.dbinfo.DBConnectVO;

/**
 * 使用c3p0实现的数据库连接池
 * 
 * @author haoooooj
 */
public class DBConnectionPool {
	/**
	 * 数据源
	 */
	private static Map<String,DataSource> dataSources = new HashMap<String,DataSource>();
	private static Map<String, DBConnectVO> dbConnectMap = new HashMap<>();
	
	static {
		try {
			
			Properties properties = getProperties("conf/application-db.properties");
			String dbs = properties.getProperty("dbs");
			if(dbs != null)
			{
				String[] db = dbs.split(",");
				for(String dbName:db)
				{
					DBConnectVO dbc = new DBConnectVO();
					dbc.setDriver(properties.getProperty(dbName+".driver"));
					dbc.setUrl(properties.getProperty(dbName+".url"));
					dbc.setUser(properties.getProperty(dbName+".username"));
					dbc.setPassword(properties.getProperty(dbName+".password"));
					if(dbc.getUrl().indexOf("oracle") != -1) {
						dbc.setDbType("ORACLE");
					} else if(dbc.getUrl().indexOf("mysql") != -1) {
						dbc.setDbType("MYSQL");
						int b = dbc.getUrl().lastIndexOf("/")+1;
						int e = dbc.getUrl().indexOf("?");
						if(e == -1) {
							e = dbc.getUrl().length();
						}
						String sc = dbc.getUrl().substring(b, e);
						dbc.setSchema(sc);
					}else if(dbc.getUrl().indexOf("kingbase") != -1) {
						dbc.setDbType("KINGBASE");
					}
					
					System.out.println(dbc.toString());
					Class.forName(dbc.getDriver());
					DataSource unpooledDataSource = com.mchange.v2.c3p0.DataSources
							.unpooledDataSource(dbc.getUrl(), dbc.getUser(), dbc.getPassword());
		
					//Properties p = Configuration.load("c3p0.properties");
		
					DataSource dataSource = com.mchange.v2.c3p0.DataSources.pooledDataSource(
							unpooledDataSource, properties);
					dataSources.put(dbName, dataSource);
					dbConnectMap.put(dbName, dbc);
				}
			}
			// dataSource = new ComboPooledDataSource();
			//			
			//			
			// dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
			// dataSource.setJdbcUrl("jdbc:oracle:thin:@10.249.13.46:1521:ZDG");
			// dataSource.setUser("dgsms1");
			// dataSource.setPassword("dgsms123");
			// dataSource.setMaxPoolSize(20);
			// dataSource.setMinPoolSize(5);
			// dataSource.setAcquireIncrement(2);

			// dataSource.setTestConnectionOnCheckin(false);
			// dataSource.setTestConnectionOnCheckout(false);
			// dataSource.setTestConnectionOnCheckout(false);

			//			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static Properties getProperties(String fileName){
		InputStream is=null;
		try{
			is = DBConnectionPool.class.getResourceAsStream(fileName);
			if(is == null)
			{
				is = DBConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
			}
		}catch(Exception e){
			throw new RuntimeException("读取配置文件失败："+fileName);
		}
		
		Properties properties=new Properties();
		try {
			properties.load(is);
			if(is!=null)is.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return properties;
	}
	
	/**
	 * 获取数据库连接
	 * 
	 * @return conn
	 */
	static Connection getConnection(String dbName) {
		Connection conn = null;
		try {
			DataSource dataSource = dataSources.get(dbName);
			if(dataSource != null)
			{
				conn = dataSource.getConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("getConnection", e);
		}
		return conn;
	}
	
	public static DBConnectVO getConnectInfo(String dbName) {
		return dbConnectMap.get(dbName);
	}

}
