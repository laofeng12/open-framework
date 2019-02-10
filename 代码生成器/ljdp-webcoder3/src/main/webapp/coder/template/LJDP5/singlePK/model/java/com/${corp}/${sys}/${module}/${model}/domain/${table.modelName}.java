<#if model == "">
package com.${corp}.${sys}.${module}.domain;
<#else>
package com.${corp}.${sys}.${module}.${model}.domain;
</#if>

import java.util.Date;

import javax.persistence.*;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author ${author}
 *
 */
@ApiModel("${tableModelName}")
@Entity
@Table(name = "${table.code}")
public class ${table.modelName} extends BasicApiResponse implements ApiResponse {
	
	<#list table.columnList as item>
	@ApiModelProperty("${item.comment}")
	private ${item.javaDataType} ${item.columnName};
	<#if item.userDict == true>
	@ApiModelProperty("${item.comment}名称")
	private String ${item.columnName}Name;
	</#if>
	
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
	
	<#if item.userDict == true>
	@Transient
	public String get${item.columnName?cap_first}Name() {
		return ${item.columnName}Name;
	}
	public void set${item.columnName?cap_first}Name(String ${item.columnName}Name) {
		this.${item.columnName}Name = ${item.columnName}Name;
	}
	</#if>
	</#if>
	</#list>
}