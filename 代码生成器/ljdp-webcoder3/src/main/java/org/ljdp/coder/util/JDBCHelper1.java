package org.ljdp.coder.util;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * ��jdbc������ݿ�İ�����
 * @author haoooooj
 */
public class JDBCHelper1 {
	
	/**
	 * �����ӳ��л�ȡһ������
	 * @return Connection
	 */
	public  static Connection getConnection(String dbName){
		Connection conn;
		try
		{
			conn=DBConnectionPool.getConnection(dbName);
		}
		catch (Exception e)
		{
			throw new RuntimeException("getConnection", e);
		}
		return conn;
	}

	
	
	/**
	 * ����ع�?
	 * @param conn
	 */
	public static void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * �����ӷŻ����ӳأ��ر�stmt�ͽ��?
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn,Statement stmt,ResultSet rs){
		
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * �����ӷŻ����ӳ�
	 * @param conn
	 */
	public static void close(Connection conn){
		close(conn, null, null);
	}
	/**
	 * �ر�stmt
	 * @param stmt
	 */
	public static void close(Statement stmt){
		close(null, stmt, null);
	}
	/**
	 * �رս��?
	 * @param rs
	 */
	public static void close(ResultSet rs){
		close(null, null, rs);
	}

}
