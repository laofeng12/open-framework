<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.query.${table.modelName}DBParam;
</#if>
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.${model}.query.${table.modelName}DBParam;
</#if>
</#if>

/**
 * ${tableModelName}业务层接口
 * @author ${author}
 *
 */
public interface ${table.modelName}Service extends IService<${table.modelName}>{
	
	<#if baseFun.query == "on">
	IPage<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable);
	</#if>
	
	<#if baseFun.add == "on" || baseFun.importFun == "on">
	</#if>
	
	<#if baseFun.delete == "on">
	</#if>
}
