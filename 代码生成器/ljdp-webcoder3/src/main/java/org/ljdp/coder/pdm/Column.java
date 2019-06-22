package org.ljdp.coder.pdm;

import org.ljdp.coder.extjs.ExtJsFieldType;
import org.ljdp.coder.util.StringUtils;
import org.ljdp.component.bean.BaseVO;


public class Column  extends BaseVO{
	
	private String name;
	private String code;//数据库字段名称
	private String comment;
	private String dataType;
	private boolean iskey = false;
	
	private boolean userDict = false;//是否使用数据字典
	private String dictDefined;//数据字典定义
	private boolean sort = false;//是否排序
	
	private int scale = 0;
	private int precision = 10;
	
	private long maxnumber;//数字类型可以保存的最大值
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComment() {
		if(comment == null || "".equals(comment)){
			return name;
		}		
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public boolean isIskey() {
		return iskey;
	}
	public void setIskey(boolean iskey) {
		this.iskey = iskey;
	}
	
	/**
	 * 数据库的类型转换为java类型
	 */
	public String getJavaDataType(){
		if(dataType == null || "".equals(dataType)){
			return DataType.STRING;
		}
		String type = dataType;
		if(dataType.indexOf("(") != -1){
			type = dataType.substring(0,dataType.indexOf("(") + 1);
		}
		return JdbcDataType.forType(type.toLowerCase(), precision);
	}
	
	/**
	 * 数据库类型转换为ExtJs的类型
	 * @return
	 */
	public String getExtJsFieldType() {
		if(dataType == null || "".equals(dataType)){
			return DataType.STRING;
		}
		String type = dataType;
		if(dataType.indexOf("(") != -1){
			type = dataType.substring(0,dataType.indexOf("(") + 1);
		}
		return ExtJsFieldType.forType(type.toLowerCase());
	}
	
	/**
	 * 转换为代码中的字段名
	 * @return
	 */
	public String getColumnName(){
		String columnName = "";
		if(code == null || "".equals(code)){
			return columnName;
		}
		String[] codes = code.split("_");
		for (int i = 0; i < codes.length; i++) {
			String code_ = codes[i].toLowerCase();
			if(i >= 1){
				code_ = StringUtils.capitalize(codes[i].toLowerCase());
			}
			columnName += code_;
		}
		return columnName;		
	}
	
	public static void main(String[] args){
		Column c = new Column();
		c.setDataType("NUMBER");
		System.out.println(c.getJavaDataType());
		
	}
	public boolean isUserDict() {
		return userDict;
	}
	public void setUserDict(boolean userDict) {
		this.userDict = userDict;
	}
	public String getDictDefined() {
		return dictDefined;
	}
	public void setDictDefined(String dictDefined) {
		this.dictDefined = dictDefined;
	}
	public boolean isSort() {
		return sort;
	}
	public void setSort(boolean sort) {
		this.sort = sort;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	public String getDictDefinedName() {
		String temp = dictDefined;
		temp = temp.replaceAll("\\.", "");
		return temp;
	}
	public long getMaxnumber() {
		return maxnumber;
	}
	public void setMaxnumber(long maxnumber) {
		this.maxnumber = maxnumber;
	}
	
	public boolean getIsNumber() {
		String javaType = getJavaDataType();
		if(javaType.equals(DataType.INTEGER)
				|| javaType.equals(DataType.LONG)
				|| javaType.equals(DataType.FLOAT)
				|| javaType.equals(DataType.DOUBLE)
				|| javaType.equals(DataType.SHORT)) {
			return true;
		}
		return false;
	}
}
