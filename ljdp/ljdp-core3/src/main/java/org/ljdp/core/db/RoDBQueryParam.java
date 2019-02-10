package org.ljdp.core.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ljdp.component.bean.BaseVO;
import org.ljdp.core.query.Condition;
import org.ljdp.core.query.Parameter;

import springfox.documentation.annotations.ApiIgnore;

/**
 * DBQueryParam的简化版，去掉分页，排序等
 * @see org.ljdp.core.db.DBQueryParam
 * @author Administrator
 *
 */
public class RoDBQueryParam extends BaseVO {

	private List<Condition> queryConditions;
	
	private List<String> selectFields;
	
	private Map<String, Parameter> paramTable;
	
	private String tableAlias;//表在sql中的别名
	
	public void addQueryCondition(String field, String operType, Object value) {
	    if (queryConditions == null) {
	        queryConditions = new ArrayList<Condition>();
	    }
	    Condition param = new Condition(field, operType, value);
	    queryConditions.add(param);
	}

	public void addSelectField(String field) {
	    if(selectFields == null) {
	        selectFields = new ArrayList<String>();
	    }
	    selectFields.add(field);
	}
	
	public void addParameter(String name, Object value) {
		if(paramTable == null) {
			paramTable = new HashMap<String, Parameter>();
		}
		paramTable.put(name, new Parameter(name, value));
	}
	
	@ApiIgnore()
	public Object getParamValue(String name) {
		Parameter param = paramTable.get(name);
		return param.getValue();
	}
	
	@ApiIgnore()
	public Collection<Parameter> getAllParam() {
		if(paramTable != null) {
			return paramTable.values();
		}
		return null;
	}
	
	@ApiIgnore()
	public String getUniqueParamName(String name) {
		return findNotContainKey(name, 0);
	}
	
	public String findNotContainKey(String name, int i) {
		String newName = name + i;
		if(paramTable != null && paramTable.containsKey(newName)) {
			return findNotContainKey(name, ++i);
		}
		return newName;
	}

	@ApiIgnore()
	public List<Condition> getQueryConditions() {
	    return queryConditions;
	}

	public void setQueryConditions(List<Condition> paramList) {
	    this.queryConditions = paramList;
	}

	@ApiIgnore()
	public List<String> getSelectFields() {
	    return selectFields;
	}

	public void setSelectFields(List<String> selectFields) {
	    this.selectFields = selectFields;
	}

	@ApiIgnore()
	public Map<String, Parameter> getParamTable() {
		return paramTable;
	}

	public void setParamTable(Map<String, Parameter> paramTable) {
		this.paramTable = paramTable;
	}

	@ApiIgnore()
	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

}
