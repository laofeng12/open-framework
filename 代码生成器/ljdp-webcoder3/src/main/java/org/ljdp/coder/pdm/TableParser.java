/**
 * 
 */
package org.ljdp.coder.pdm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ljdp.coder.dbinfo.DBConnectVO;
import org.ljdp.coder.util.DBConnectionPool;
import org.ljdp.coder.util.JDBCHelper1;

/**
 * @author liaojinhua
 *
 */
public class TableParser {
	
	public static Table parse(String dbName, String tableName,String modelName, Map<String, String> dictMap,
			int rowColNum) {
		Connection connection = null;
		Statement stmt = null;
		PreparedStatement  pstmt =null;
		try{
	        Table table = null;
			connection = JDBCHelper1.getConnection(dbName);
//			DatabaseMetaData md = connection.getMetaData();
			//解析表字段注释
			Map commentMap = parserColumnComment(connection, dbName, tableName);
            
			//rs = md.getCatalogs();
			//rs.getString("TABLE_CAT")
			stmt = connection.createStatement();
			
			String tableSql = "select * from "+tableName;
			DBConnectVO info = DBConnectionPool.getConnectInfo(dbName);
			if(info.getDbType().equals("ORACLE")) {
				tableSql = "select * from "+tableName+" where rownum=1";
			} else if(info.getDbType().equals("MYSQL")) {
				tableSql = "select * from "+tableName+" LIMIT 1";
			}else if(info.getDbType().equals("KINGBASE")) {
				tableSql = "select * from \"" +tableName+ "\" LIMIT 1";
			}

			ResultSet rs = stmt.executeQuery(tableSql);
			ResultSetMetaData rsm = rs.getMetaData();
			int count = rsm.getColumnCount();
			table = new Table();
			table.setName(tableName);
			table.setCode(tableName.toUpperCase());
			if(modelName != null){
				table.setModelName(modelName);
			}
			table.setSchema(info.getSchema());
			
			List<Column> columnList = new ArrayList<Column>();
			/**
			 * 3、读取表注释：SQL>select * from user_tab_comments where comments is not null; 
			 * 4、读取列注释：SQL>select * from user_col_commnents where comments is not null and table_name='表名'; 
			 */
			for(int i=1;i<=count;i++) {
				Column c = new Column();
				String name = rsm.getColumnName(i);
				String desc =  (String)commentMap.get(rsm.getColumnLabel(i));
				if(desc == null) {
					desc = name;
				}
				String typeName =rsm.getColumnTypeName(i);
				if(typeName.indexOf(" ") > 0) {
					typeName = typeName.substring(0, typeName.indexOf(" "));
				}
				c.setCode(name);
				c.setComment(desc.replaceAll("\n", " "));
				c.setDataType(typeName);
				c.setName(name);
				c.setScale(rsm.getScale(i));
				c.setPrecision(rsm.getPrecision(i));
				if(dictMap != null && dictMap.containsKey(name)) {
					c.setUserDict(true);
					c.setDictDefined(dictMap.get(name));
				}
				//获取数字类型最大的值
				if(c.getIsNumber()) {
					Double t = Math.pow(10, c.getPrecision());
					long t2 = t.longValue()-1;
					c.setMaxnumber(t2);
				}
				columnList.add(c);
				System.out.println(name+":"+typeName+"("+c.getPrecision()+","+c.getScale()+")"+" - "+c.getJavaDataType());
			}
			table.setColumnList(columnList);
			rs.close();
			
			//解析主键字段
			parserTablePrimarykey(dbName, connection, table);
			if(table.getKeyList().size() == 0) {
				//没有主键，先默认第一个字段为主键吧
				Column c = (Column) columnList.get(0);
				table.setKeyFieldType(c.getJavaDataType());
				table.setKeyField(c.getColumnName());
			}
			
			//把字段按${rowColNum}个一行进行拆分，以便在页面显示
			List<List<Column>> rowCloumnList = new ArrayList<List<Column>>();
			for (int i = 0; i < columnList.size(); ) {
				List<Column> rowlist = new ArrayList<Column>();
				for(int j=0; i < columnList.size() && j < rowColNum; i++) {
					Column c = columnList.get(i);
					if(c.isIskey()) {
						//主键不显示在编辑页面中
					} else {
						rowlist.add(c);
						j++;
					}
				}
				rowCloumnList.add(rowlist);
			}
			table.setRowCloumnList(rowCloumnList);
            
			return table;
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取主键字段
	 * @param dbName
	 * @param connection
	 * @param table
	 * @throws SQLException
	 */
	protected static void parserTablePrimarykey(String dbName, Connection connection, Table table) throws SQLException {
		DBConnectVO info = DBConnectionPool.getConnectInfo(dbName);
		Map<String, Column> keyMap = new HashMap<String, Column>();//保存主键信息
		List<Column> keyList = new ArrayList<Column>();//保存主键信息
		
		ResultSet rs = null;
		if(info.getDbType().equals("ORACLE")) {
			String querySQL = "select * from user_cons_columns where constraint_name=("
					+ "select constraint_name from user_constraints"
					+ " where table_name=? and constraint_type='P')";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, table.getName().toUpperCase());
			rs = pstmt.executeQuery();
		} else if(info.getDbType().equals("MYSQL")) {
			String querySQL = "SELECT c.COLUMN_NAME,c.ORDINAL_POSITION "+
					"FROM"+
					"  INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t,"+
					"  INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c "+
					"WHERE"+
					"  t.TABLE_NAME=c.TABLE_NAME and t.TABLE_SCHEMA=c.TABLE_SCHEMA and t.CONSTRAINT_NAME=c.CONSTRAINT_NAME"+
					"  AND t.CONSTRAINT_TYPE = 'PRIMARY KEY'"+
					"  AND t.TABLE_SCHEMA =? and t.TABLE_NAME=? ";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, table.getSchema());
			pstmt.setString(2, table.getName().toUpperCase());
			rs = pstmt.executeQuery();
		}else if(info.getDbType().equals("KINGBASE")){
			String querySQL = "SELECT c.COLUMN_NAME,c.ORDINAL_POSITION "+
					"FROM"+
					"  INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t,"+
					"  INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c "+
					"WHERE"+
					"  t.TABLE_NAME=c.TABLE_NAME and t.TABLE_SCHEMA=c.TABLE_SCHEMA and t.CONSTRAINT_NAME=c.CONSTRAINT_NAME"+
					"  AND t.CONSTRAINT_TYPE = 'PRIMARY KEY'"+
					"  AND t.table_catalog =? and t.TABLE_NAME=? ";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, table.getSchema());
			pstmt.setString(2, table.getName());
			rs = pstmt.executeQuery();
		}
		if(rs != null) {
			while(rs.next()){
				String keyname = rs.getString("COLUMN_NAME");
				
				for (int i = 0; i < table.getColumnList().size(); i++) {
					Column c = (Column) table.getColumnList().get(i);
					if(c.getName().equals(keyname)) {
						c.setIskey(true);
						keyMap.put(keyname, c);
						keyList.add(c);
						table.setKeyFieldType(c.getJavaDataType());
						table.setKeyField(c.getColumnName());
					}
				}
			}
			rs.close();
		}
		table.setKeyMap(keyMap);
		table.setKeyList(keyList);
	}

	protected static Map parserColumnComment(Connection connection, String dbName, String tableName) throws SQLException, Exception {
		Map commentMap = null;
		DBConnectVO info = DBConnectionPool.getConnectInfo(dbName);
		if(info.getDbType().equals("ORACLE")) {
			String querySQL = "select TABLE_NAME,COLUMN_NAME,COMMENTS "
					+ "FROM USER_COL_COMMENTS where TABLE_NAME=?";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, tableName.toUpperCase());
			ResultSet rs1 = pstmt.executeQuery();
			commentMap = getTableObjectMap(rs1, "COLUMN_NAME", "COMMENTS");
			rs1.close();
		} else if(info.getDbType().equals("MYSQL")) {
			String querySQL = "select t.TABLE_NAME,t.COLUMN_NAME,t.COLUMN_COMMENT "
					+ "from information_schema.columns t "
					+ "where table_schema =? "
					+ "and table_name = ?";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, info.getSchema().toLowerCase());
			pstmt.setString(2, tableName.toLowerCase());
			ResultSet rs1 = pstmt.executeQuery();
			commentMap = getTableObjectMap(rs1, "COLUMN_NAME", "COLUMN_COMMENT");
			rs1.close();
		}else if(info.getDbType().equals("KINGBASE")) {
			String querySQL ="SELECT t.TABLE_NAME,t.COLUMN_NAME,s.DESCRIPTION FROM information_schema.columns t , SYS_DESCRIPTION s ,SYS_ATTRIBUTE a,SYS_CLASS y "
					+" where s.OBJOID=a.ATTRELID and  y.OID=a.ATTRELID and a.ATTNUM=s.OBJSUBID and a.ATTNAME=t.COLUMN_NAME and y.RELNAME= t.TABLE_NAME and "
					+" t.table_catalog=? and t.TABLE_NAME=?  ";
			PreparedStatement pstmt = connection.prepareStatement(querySQL);
			pstmt.setString(1, info.getSchema());
			pstmt.setString(2, tableName);
			ResultSet rs1 = pstmt.executeQuery();
			commentMap = getTableObjectMap(rs1, "COLUMN_NAME", "DESCRIPTION");
			rs1.close();
		}
		
		return commentMap;
	}
	
	public static Map getTableObjectMap(ResultSet rs,String s1,String s2) throws Exception {   
        Map resultMap = new HashMap();   
        while(rs.next()) {   
            String key = "";   
            String value = "";   
            key = rs.getString(s1);   
            value = rs.getString(s2); 
            resultMap.put(key, value);   
        }   
        return resultMap;   
    }   

}
