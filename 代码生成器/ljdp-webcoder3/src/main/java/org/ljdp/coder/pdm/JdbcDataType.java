package org.ljdp.coder.pdm;

import java.util.HashMap;
import java.util.Map;

public enum JdbcDataType {
	TINYINT(DataType.BYTE),
	SMALLINT(DataType.SHORT),
	INTEGER(DataType.INTEGER),
	INT(DataType.INTEGER),
	BIGINT(DataType.LONG),
	FLOAT(DataType.FLOAT),
	DOUBLE(DataType.DOUBLE),
	DECIMAL(DataType.DOUBLE),
	CHAR(DataType.STRING),
	VARCHAR(DataType.STRING),
	TEXT(DataType.STRING),
	CLOB(DataType.STRING),
	BLOB(DataType.BYTEArray),
	TIMESTAMP(DataType.DATE),
	DATE(DataType.DATE),
	TIME(DataType.DATE),
	DATETIME(DataType.DATE),
	BOOLEAN(DataType.BYTE),
	NUMBER(DataType.LONG);
	
	public final String JAVA_TYPE;
	private static Map<String,String> typeLookup = new HashMap<String,String>();
	
	static {
		for (JdbcDataType type : JdbcDataType.values()) {
			typeLookup.put(type.toString().toLowerCase(),type.JAVA_TYPE);
		}
	}

	private JdbcDataType(String type) {
		this.JAVA_TYPE = type;
	}

	public static String forType(String jdbcType, int precision)  {
		String javaType = typeLookup.get(jdbcType);
		if(javaType == null  || "".equals(javaType)){
			javaType = DataType.STRING;
		}
		if(jdbcType.equals(JdbcDataType.DECIMAL.toString().toLowerCase())) {
			if(precision <= 8) {
				javaType = DataType.FLOAT;
			}
		}
		return javaType;
	}
}
