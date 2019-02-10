package org.ljdp.coder.pdm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ljdp.coder.util.JDBCHelper1;

public class DBInfoUtils {

	public static Long getResSequence()  throws Exception{
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JDBCHelper1.getConnection("default");
			Statement st = con.createStatement();
			rs = st.executeQuery("SELECT seq_sys_res.nextval FROM dual");
			rs.next();
			Long s = rs.getLong(1);
			
			return s;
		} finally{
			try {
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
