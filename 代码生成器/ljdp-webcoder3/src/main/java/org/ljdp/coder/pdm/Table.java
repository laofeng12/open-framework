package org.ljdp.coder.pdm;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.ljdp.coder.util.StringUtils;
import org.ljdp.component.bean.BaseVO;

public class Table extends BaseVO{
	
	private String schema;//数据库实例名，模式名等
	private String name;//描述
	private String code;//数据库字段
	private String comment;
	private String creator;
	private Long creationDate;
	private Column primaryColumn;//�����ֶ�
	private List<Column> columnList;
	private List<List<Column>> rowCloumnList;//把字段按4个一行进行拆分
	private String modelName;
	public String keyField;//主键
	public String keyFieldType;//主键java类型
	private Map<String, Column> keyMap;
	private List<Column> keyList;
	
	public String getModelName() {
		if(modelName !=null && !modelName.equals("")){
			return modelName;
		}else{
			String ret = code;
			if(code != null ){
				String tmp = code.toLowerCase();
				if(tmp.startsWith("tbl")){
					tmp = tmp.substring(3);
				}else if(tmp.startsWith("t")){
					tmp = tmp.substring(1);
				}
				ret = "";
				String[] codes = tmp.split("_");
				for (int i = 0; i < codes.length; i++) {
					ret += StringUtils.capitalize(codes[i].toLowerCase());
				}
				
			}
			return ret;
		}
	}
	
	public String getModelName2() {
		return StringUtils.uncapitalize(getModelName());
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getKeyField() {
		return keyField;
	}
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
	
	public String getCreationDate() {
		SimpleDateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd");
		return dateformat.format(creationDate);
	}
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	public Column getPrimaryColumn() {
		return primaryColumn;
	}
	public void setPrimaryColumn(Column primaryColumn) {
		this.primaryColumn = primaryColumn;
	}
	public List<Column> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<Column> columnList) {
		this.columnList = columnList;
	}
	
	public String getTableName(){
		String tableName = "";
		if(code == null || "".equals(code)){
			return tableName;
		}
		String[] codes = code.split("_");
		for (int i = 0; i < codes.length; i++) {
			tableName += StringUtils.capitalize(codes[i].toLowerCase());
		}
		return tableName;
	}

	public Map<String, Column> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(Map<String, Column> keyList) {
		this.keyMap = keyList;
	}

	public List<Column> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<Column> keyList) {
		this.keyList = keyList;
	}

	public String getKeyFieldType() {
		return keyFieldType;
	}

	public void setKeyFieldType(String keyFieldType) {
		this.keyFieldType = keyFieldType;
	}

	public List<List<Column>> getRowCloumnList() {
		return rowCloumnList;
	}

	public void setRowCloumnList(List<List<Column>> rowCloumnList) {
		this.rowCloumnList = rowCloumnList;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}


}
