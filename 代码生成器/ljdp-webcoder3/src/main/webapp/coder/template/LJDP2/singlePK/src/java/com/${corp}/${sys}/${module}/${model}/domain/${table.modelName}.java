<#if model == "">
package com.${corp}.${sys}.${module}.domain;
<#else>
package com.${corp}.${sys}.${module}.${model}.domain;
</#if>

import java.util.Date;

import javax.persistence.*;

import org.ljdp.component.bean.BaseVO;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实体
 * @author ${author}
 *
 */
@Entity
@Table(name = "${table.code}")
public class ${table.modelName} extends BaseVO implements java.io.Serializable {
	
	<#list table.columnList as item>
	private ${item.javaDataType} ${item.columnName};//${item.comment}
	</#list>
	
	<#list table.keyMap?keys as k>
	@Id
	@Column(name = "${table.keyMap[k].name}")
	public ${table.keyMap[k].javaDataType} get${table.keyMap[k].columnName?cap_first}() {
		return ${table.keyMap[k].columnName};
	}
	public void set${table.keyMap[k].columnName?cap_first}(${table.keyMap[k].javaDataType} ${table.keyMap[k].columnName}) {
		this.${table.keyMap[k].columnName} = ${table.keyMap[k].columnName};
	}
	</#list>
	
	<#list table.columnList as item>
	<#if item.iskey == false>
	<#if item.javaDataType == "Date">
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)</#if>
	@Column(name = "${item.name}")
	public ${item.javaDataType} get${item.columnName?cap_first}() {
		return ${item.columnName};
	}
	public void set${item.columnName?cap_first}(${item.javaDataType} ${item.columnName}) {
		this.${item.columnName} = ${item.columnName};
	}
	</#if>
	</#list>
}