<#if model == "">
package com.${corp}.${sys}.${module}.repository;
<#else>
package com.${corp}.${sys}.${module}.${model}.repository;
</#if>

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
</#if>

/**
 * ${tableModelName}数据库访问层
 * @author ${author}
 *
 */
public interface ${table.modelName}Repository extends JpaRepository<${table.modelName}, ${table.keyFieldType}>, JpaSpecificationExecutor<${table.modelName}>, ${table.modelName}RepositoryCustom{
	
}
