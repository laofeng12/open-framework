<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import javax.annotation.Resource;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.query.${table.modelName}DBParam;
</#if>
import com.${corp}.${sys}.${module}.repository.${table.modelName}Repository;
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.${model}.query.${table.modelName}DBParam;
</#if>
import com.${corp}.${sys}.${module}.${model}.repository.${table.modelName}Repository;
</#if>

/**
 * 业务层
 * @author ${author}
 *
 */
@Service
@Transactional
public class ${table.modelName}ServiceImpl implements ${table.modelName}Service {
	
	@Resource
	private ${table.modelName}Repository ${table.modelName2}Repository;
	
	<#if baseFun.query == "on">
	public Page<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable){
		return ${table.modelName2}Repository.query(params, pageable);
	}
	
	public List<${table.modelName}> queryDataOnly(${table.modelName}DBParam params, Pageable pageable){
		return ${table.modelName2}Repository.queryDataOnly(params, pageable);
	}
	</#if>
	
	public ${table.modelName} get(${table.keyFieldType} id) {
		return ${table.modelName2}Repository.findOne(id);
	}
	
	<#if baseFun.add == "on" || baseFun.importFun == "on">
	public ${table.modelName} doSave(${table.modelName} m) {
		return ${table.modelName2}Repository.save(m);
	}
	</#if>
	
	<#if baseFun.delete == "on">
	public void doDelete(${table.keyFieldType} id) {
		${table.modelName2}Repository.deleteById(id);
	}
	</#if>
}
