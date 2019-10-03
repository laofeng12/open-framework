package org.ljdp.coder.pdm;

import org.ljdp.component.bean.BaseVO;


/**
 * 数据库查询条件
 * @author hzy
 *
 */
public class DbParam extends BaseVO{

	private String condition;//查询条件标识符
	private String code;//数据库中的字段
	private String name;//字段名称
	private String symbol;//查询条件的符合
	
	private String javaDataType;//java的字段类型
	private String useJavaType;//对应查询参数使用的类型
	private String extJsFieldType;//ExtJs的字段类型
	private String columnName;//java的字段名
	
	private String dictDefined;//数据字典定义
	
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getJavaDataType() {
		return javaDataType;
	}
	public void setJavaDataType(String javaDataType) {
		this.javaDataType = javaDataType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getExtJsFieldType() {
		return extJsFieldType;
	}
	public void setExtJsFieldType(String extJsFieldType) {
		this.extJsFieldType = extJsFieldType;
	}
	public String getDictDefined() {
		return dictDefined;
	}
	public void setDictDefined(String dictDefined) {
		this.dictDefined = dictDefined;
	}
	public String getUseJavaType() {
		return useJavaType;
	}
	public void setUseJavaType(String useJavaType) {
		this.useJavaType = useJavaType;
	}
	
}
