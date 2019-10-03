<#if model == "">
package com.${corp}.${sys}.${module}.repository;
<#else>
package com.${corp}.${sys}.${module}.${model}.repository;
</#if>

public interface ${table.modelName}RepositoryCustom {

}
