<#if model == "">
package com.${corp}.${sys}.${module}.repository;
<#else>
package com.${corp}.${sys}.${module}.${model}.repository;
</#if>

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
</#if>

/**
 * 数据库访问层
 * @author ${author}
 *
 */
public interface ${table.modelName}Repository extends DynamicJpaRepository<${table.modelName}, ${table.keyFieldType}>, ${table.modelName}RepositoryCustom{
	<#if baseFun.delete == "on">
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete ${table.modelName} t where t.${table.keyField}=:${table.keyField}")
	public int deleteById(@Param("${table.keyField}")${table.keyFieldType} ${table.keyField});
	</#if>
}
