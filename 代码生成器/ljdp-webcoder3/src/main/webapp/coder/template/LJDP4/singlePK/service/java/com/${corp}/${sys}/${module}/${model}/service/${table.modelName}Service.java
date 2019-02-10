<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
 * 业务层接口
 * @author ${author}
 *
 */
public interface ${table.modelName}Service {
	<#if baseFun.query == "on">
	public Page<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable);
	
	public List<${table.modelName}> queryDataOnly(${table.modelName}DBParam params, Pageable pageable);
	</#if>
	
	public ${table.modelName} get(${table.keyFieldType} id);
	
	<#if baseFun.add == "on" || baseFun.importFun == "on">
	public ${table.modelName} doSave(${table.modelName} m);
	</#if>
	
	<#if baseFun.delete == "on">
	public void doDelete(${table.keyFieldType} id);
	public void doRemove(String ids);
	</#if>
}
