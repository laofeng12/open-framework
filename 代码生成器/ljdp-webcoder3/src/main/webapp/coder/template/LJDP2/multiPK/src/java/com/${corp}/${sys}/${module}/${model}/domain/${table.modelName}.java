<#if model == "">
package com.${corp}.${sys}.${module}.domain;
<#else>
package com.${corp}.${sys}.${module}.${model}.domain;
</#if>

import java.util.Date;

import javax.persistence.*;

import org.ljdp.core.pojo.BaseVO;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 复合主键实体
 */
@Entity
@Table(name = "${table.code}")
public class ${table.modelName} extends BaseVO implements java.io.Serializable {

	// Fields

	private ${table.modelName}Id pk;
	
	<#list table.columnList as item>
	<#if item.iskey == false>
	private ${item.javaDataType} ${item.columnName};//${item.comment}
	</#if>
	</#list>

	// Constructors

	/** default constructor */
	public ${table.modelName}() {
	}

	/** full constructor */
	public ${table.modelName}(${table.modelName}Id id) {
		this.pk = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
		<#list table.keyList as item>
			@AttributeOverride(name = "${item.columnName}", column = @Column(name = "${item.name}", nullable = false))<#if item_has_next>,</#if>
		</#list>
		})
	public ${table.modelName}Id getPk() {
		return this.pk;
	}

	public void setPk(${table.modelName}Id id) {
		this.pk = id;
	}
	
	<#list table.columnList as item>
	<#if item.iskey == false>
	<#if item.javaDataType == "Date">
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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