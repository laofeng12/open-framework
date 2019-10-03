<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.query.${table.modelName}DBParam;
</#if>
import com.${corp}.${sys}.${module}.mapper.${table.modelName}Mapper;
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.${model}.query.${table.modelName}DBParam;
</#if>
import com.${corp}.${sys}.${module}.${model}.mapper.${table.modelName}Mapper;
</#if>
<#if userDict == true>
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
</#if>
/**
 * ${tableModelName}业务层
 * @author ${author}
 *
 */
@Service
public class ${table.modelName}ServiceImpl extends ServiceImpl<${table.modelName}Mapper, ${table.modelName}> implements ${table.modelName}Service {
	
	
	<#if userDict == true>
	@Resource
	private SysCodeService sysCodeService;
	</#if>
	
	<#if baseFun.query == "on">
	public IPage<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable){
		//JPA的page从0开始，MyBatisPlus的page从1开始。前端统一用从0开始。所以这里需+1
		IPage<${table.modelName}> mypage = new Page<>(pageable.getPageNumber()+1, pageable.getPageSize());
		
		IPage<${table.modelName}> page = this.page(
				mypage,
				new QueryWrapper<${table.modelName}>()
				<#list dbParamList as item>
				<#if item.useJavaType == "String">
					.${item.condition}(StringUtils.isNotBlank(params.get${item.condition?cap_first}_${item.columnName}()),"${item.code}", params.get${item.condition?cap_first}_${item.columnName}())
				<#else>
					.${item.condition}(params.get${item.condition?cap_first}_${item.columnName}() != null,"${item.code}", params.get${item.condition?cap_first}_${item.columnName}())
				</#if>
				</#list>
			);
		<#if userDict == true>
		<#list table.columnList as item>
		<#if item.userDict == true>
		Map<String, SysCode> ${item.dictDefinedName} = sysCodeService.getCodeMap("${item.dictDefined}");
		</#if>
		</#list>
		for (${table.modelName} m :  page.getRecords()) {
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
		return page;
	}
	</#if>
	
}
