<#setting number_format="#">
<#if model == "">
package com.${corp}.${sys}.${module}.domain;
<#else>
package com.${corp}.${sys}.${module}.${model}.domain;
</#if>

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Persistable;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author ${author}
 *
 */
@ApiModel("${tableModelName}")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "${table.code}")
public class ${table.modelName} implements Persistable<${table.keyFieldType}>,Serializable {
	
	<#list table.columnList as item>
	@ApiModelProperty("${item.comment}")<#if item.iskey == true>
	@Id<#else><#if item.javaDataType == "String">
	@Length(min=0, max=${item.precision})</#if><#if item.isNumber == true>
	@Max(${item.maxnumber}L)</#if><#if item.javaDataType == "Date">
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)</#if></#if>
	@Column(name = "${item.name}")
	private ${item.javaDataType} ${item.columnName};
	<#if item.userDict == true>
	@ApiModelProperty("${item.comment}名称")
	@Transient
	private String ${item.columnName}Name;
	</#if>
	
	</#list>
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public ${table.keyFieldType} getId() {
        return this.${table.keyField};
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.${table.keyField} != null) {
    		return false;
    	}
    	return true;
    }
    
}