<#if model == "">
package com.${corp}.${sys}.${module}.query;
<#else>
package com.${corp}.${sys}.${module}.${model}.query;
</#if>

import java.util.Date;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author ${author}
 *
 */
public class ${table.modelName}DBParam extends RoDBQueryParam {
	<#list table.keyList as item>
	private ${item.javaDataType} eq_${item.columnName};//${item.comment} --主键查询
	</#list>
	
	<#list dbParamList as item>
	<#if item.javaDataType == "Date">
	@DateTimeFormat(pattern="yyyy-MM-dd")
	</#if>
	private ${item.javaDataType} ${item.condition}_${item.columnName};//${item.name} ${item.symbol} ?
	</#list>
	
	<#list table.keyList as item>
	public ${item.javaDataType} getEq_${item.columnName}() {
		return eq_${item.columnName};
	}
	public void setEq_${item.columnName}(${item.javaDataType} ${item.columnName}) {
		this.eq_${item.columnName} = ${item.columnName};
	}
	</#list>
	
	<#list dbParamList as item>
	public ${item.javaDataType} get${item.condition?cap_first}_${item.columnName}() {
		return ${item.condition}_${item.columnName};
	}
	public void set${item.condition?cap_first}_${item.columnName}(${item.javaDataType} ${item.columnName}) {
		this.${item.condition}_${item.columnName} = ${item.columnName};
	}
	</#list>
}