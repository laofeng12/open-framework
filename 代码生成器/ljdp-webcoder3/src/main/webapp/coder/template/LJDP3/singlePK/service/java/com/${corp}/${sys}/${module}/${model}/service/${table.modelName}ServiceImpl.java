<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

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
<#if userDict == true>
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
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
	<#if userDict == true>
	@Resource
	private SysCodeService sysCodeService;
	</#if>
	
	<#if baseFun.query == "on">
	public Page<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable){
		Page<${table.modelName}> pageresult = ${table.modelName2}Repository.query(params, pageable);
		<#if userDict == true>
		<#list table.columnList as item>
		<#if item.userDict == true>
		Map<String, SysCode> ${item.dictDefinedName} = sysCodeService.getCodeMap("${item.dictDefined}");
		</#if>
		</#list>
		for (${table.modelName} m : pageresult.getContent()) {
			<#list table.columnList as item>
			<#if item.userDict>
			if(m.get${item.columnName?cap_first}() != null) {
				SysCode c = ${item.dictDefinedName}.get(m.get${item.columnName?cap_first}().toString());
				if(c != null) {
					m.set${item.columnName?cap_first}Name(c.getCodename());
				}
			}
			</#if>
			</#list>
		}
		</#if>
		return pageresult;
	}
	
	public List<${table.modelName}> queryDataOnly(${table.modelName}DBParam params, Pageable pageable){
		return ${table.modelName2}Repository.queryDataOnly(params, pageable);
	}
	</#if>
	
	public ${table.modelName} get(${table.keyFieldType} id) {
		${table.modelName} m = ${table.modelName2}Repository.findOne(id);
		<#if userDict == true>
		<#list table.columnList as item>
		<#if item.userDict == true>
		if(m.get${item.columnName?cap_first}() != null) {
			Map<String, SysCode> ${item.dictDefinedName} = sysCodeService.getCodeMap("${item.dictDefined}");
			SysCode c = ${item.dictDefinedName}.get(m.get${item.columnName?cap_first}().toString());
			if(c != null) {				
				m.set${item.columnName?cap_first}Name(c.getCodename());
			}
		}
		</#if>
		</#list>
		</#if>
		return m;
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
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			${table.modelName2}Repository.deleteById(new ${table.keyFieldType}(items[i]));
		}
	}
	</#if>
}
