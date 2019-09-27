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
 * ${tableModelName}业务层接口
 * @author ${author}
 *
 */
public interface ${table.modelName}Service {
	<#if baseFun.query == "on">
	Page<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable);
	
	List<${table.modelName}> queryDataOnly(${table.modelName}DBParam params, Pageable pageable);
	</#if>
	
	${table.modelName} get(${table.keyFieldType} id);
	
	<#if baseFun.add == "on" || baseFun.importFun == "on">
	${table.modelName} doSave(${table.modelName} m);
	</#if>
	
	<#if baseFun.delete == "on">
	void doDelete(${table.keyFieldType} id);
	void doRemove(String ids);
	</#if>
}
