<#if model == "">
package com.${corp}.${sys}.${module}.mapper;
<#else>
package com.${corp}.${sys}.${module}.${model}.mapper;
</#if>

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
@Mapper
public interface ${table.modelName}Mapper extends BaseMapper<${table.modelName}>{

}