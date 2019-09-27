<#if model == "">
package com.${corp}.${sys}.${module}.domain;
<#else>
package com.${corp}.${sys}.${module}.${model}.domain;
</#if>

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 实体复合主键
 */
@Embeddable
public class ${table.modelName}Id implements java.io.Serializable {

	// Fields
	<#list table.keyList as item>
	private ${item.javaDataType} ${item.columnName};//${item.comment}
	</#list>

	// Constructors

	/** default constructor */
	public ${table.modelName}Id() {
	}

	/** full constructor */
	public ${table.modelName}Id(
			<#list table.keyList as item>
			${item.javaDataType} ${item.columnName}<#if item_has_next>,</#if>
			</#list>) {
		<#list table.keyList as item>
		this.${item.columnName} = ${item.columnName};
		</#list>
	}

	// Property accessors
	<#list table.keyList as item>
	@Column(name = "${item.name}", nullable = false)
	public ${item.javaDataType} get${item.columnName?cap_first}() {
		return ${item.columnName};
	}
	public void set${item.columnName?cap_first}(${item.javaDataType} ${item.columnName}) {
		this.${item.columnName} = ${item.columnName};
	}
	
	</#list>

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ${table.modelName}Id))
			return false;
		${table.modelName}Id castOther = (${table.modelName}Id) other;

		return new EqualsBuilder()
		<#list table.keyList as item>
			.append(this.${item.columnName}, castOther.get${item.columnName?cap_first}())
		</#list>
	        .isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
		<#list table.keyList as item>
			.append(this.${item.columnName})
		</#list>
			.toHashCode();
	}

}