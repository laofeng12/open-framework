package org.ljdp.coder.extjs;

import java.util.HashMap;
import java.util.Map;


public enum ExtJsFieldType {
	TINYINT(FieldType.NUMBER),
	SMALLINT(FieldType.NUMBER),
	INTEGER(FieldType.NUMBER),
	INT(FieldType.NUMBER),
	BIGINT(FieldType.NUMBER),
	FLOAT(FieldType.NUMBER),
	DOUBLE(FieldType.NUMBER),
	CHAR(FieldType.TEXT),
	VARCHAR(FieldType.TEXT),
	TEXT(FieldType.TEXT),
	CLOB(FieldType.TEXT),
	BLOB(FieldType.TEXT),
	TIMESTAMP(FieldType.DATE),
	DATE(FieldType.DATE),
	TIME(FieldType.DATE),
	DATETIME(FieldType.DATE),
	BOOLEAN(FieldType.TEXT),
	NUMBER(FieldType.NUMBER);
	
	public final String EXTJS_TYPE;
	
	private ExtJsFieldType(String type) {
		this.EXTJS_TYPE = type;
	}
	
	private static Map<String,String> typeLookup = new HashMap<String,String>();
	
	static {
		for (ExtJsFieldType type : ExtJsFieldType.values()) {
			typeLookup.put(type.toString().toLowerCase(),type.EXTJS_TYPE);
		}
	}
	
	public static String forType(String jdbcType)  {
		String extjsType = typeLookup.get(jdbcType);
		if(extjsType == null  || "".equals(extjsType)){
			extjsType = FieldType.TEXT;
		}
		return extjsType;
	}
}
