<#if model == "">
package com.${corp}.${sys}.${module}.service;
<#else>
package com.${corp}.${sys}.${module}.${model}.service;
</#if>

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
 * ${tableModelName}业务层
 * @author ${author}
 *
 */
@Service
public class ${table.modelName}ServiceImpl implements ${table.modelName}Service {
	
	@Resource
	private ${table.modelName}Repository ${table.modelName2}Repository;
	<#if userDict == true>
	@Resource
	private SysCodeService sysCodeService;
	</#if>
	
	<#if baseFun.query == "on">
	public Page<${table.modelName}> query(${table.modelName}DBParam params, Pageable pageable){
		Page<${table.modelName}> pageresult = ${table.modelName2}Repository.findAll(new Specification<${table.modelName}>() {
			@Override
			public Predicate toPredicate(Root<${table.modelName}> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				<#list dbParamList as item>
				<#if item.useJavaType == "String">
				if(StringUtils.isNotBlank(params.get${item.condition?cap_first}_${item.columnName}())) {
				<#else>
				if(params.get${item.condition?cap_first}_${item.columnName}() != null) {
				</#if>
				<#switch item.condition>
				<#case "eq">
					predicates.add(criteriaBuilder.equal(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "ne">
					predicates.add(criteriaBuilder.notEqual(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "lt">
					predicates.add(criteriaBuilder.lessThan(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "le">
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "gt">
					predicates.add(criteriaBuilder.greaterThan(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "ge">
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("${item.columnName}"), params.get${item.condition?cap_first}_${item.columnName}()));
				<#break>
				<#case "like">
					predicates.add(criteriaBuilder.like(root.get("${item.columnName}"), "%"+params.get${item.condition?cap_first}_${item.columnName}()+"%"));
				<#break>
				<#case "nlike">
					predicates.add(criteriaBuilder.notLike(root.get("${item.columnName}"), "%"+params.get${item.condition?cap_first}_${item.columnName}()+"%"));
				<#break>
				<#case "null">
					if(params.get${item.condition?cap_first}_${item.columnName}()) {
						criteriaBuilder.isNull(root.get("${item.columnName}"));
					} else {
						criteriaBuilder.isNotNull(root.get("${item.columnName}"));
					}
				<#break>
				<#case "in">
					List<?> value = params.get${item.condition?cap_first}_${item.columnName}();
					Expression<?> exp = root.get("${item.columnName}");
					Predicate predicate = exp.in(((List<?>)value).toArray());
					predicates.add(predicate);
				<#break>
				<#case "nin">
					List<?> value = params.get${item.condition?cap_first}_${item.columnName}();
					Expression<?> exp = root.get("${item.columnName}");
					Predicate predicate = exp.in(((List<?>)value).toArray()).not();
					predicates.add(predicate);
				<#break>
				</#switch>
				}
				</#list>
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
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
	
	</#if>
	
	public ${table.modelName} get(${table.keyFieldType} id) {
		Optional<${table.modelName}> o = ${table.modelName2}Repository.findById(id);
		if(o.isPresent()) {
			${table.modelName} m = o.get();
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
		System.out.println("找不到记录${table.modelName}："+id);
		return null;
	}
	
	<#if baseFun.add == "on" || baseFun.importFun == "on">
	@Transactional(rollbackFor=Exception.class)
	public ${table.modelName} doSave(${table.modelName} m) {
		return ${table.modelName2}Repository.save(m);
	}
	</#if>
	
	<#if baseFun.delete == "on">
	@Transactional(rollbackFor=Exception.class)
	public void doDelete(${table.keyFieldType} id) {
		${table.modelName2}Repository.deleteById(id);
	}
	@Transactional(rollbackFor=Exception.class)
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			${table.modelName2}Repository.deleteById(new ${table.keyFieldType}(items[i]));
		}
	}
	</#if>
}
