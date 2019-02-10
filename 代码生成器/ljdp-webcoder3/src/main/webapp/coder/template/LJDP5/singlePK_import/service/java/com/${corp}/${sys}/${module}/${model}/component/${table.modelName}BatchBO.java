<#if model == "">
package com.${corp}.${sys}.${module}.component;
<#else>
package com.${corp}.${sys}.${module}.${model}.component;
</#if>

import java.util.Date;
import javax.annotation.Resource;

import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.util.DateFormater;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.springframework.stereotype.Component;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
import com.${corp}.${sys}.${module}.service.${table.modelName}Service;
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
import com.${corp}.${sys}.${module}.${model}.service.${table.modelName}Service;
</#if>

/**
 * ${tableModelName}文件导入的业务对象（前台处理）
 * 运行顺序如下：
 * initialization();
 * for(读取文件每行数据->record){
 *    doProcessRecord(record);
 * }
 * finalWork();
 * destory();
 * 
 * @author ${author}
 */
@Component
public class ${table.modelName}BatchBO extends BaseFileImportBO {

	@Resource
	private ${table.modelName}Service ${table.modelName2}Service;
	
	@Override
	public String getTitle() {
		return "<#list table.columnList as item><#if item.iskey == false>${item.comment}|</#if></#list>";
	}
	
	@Override
	protected BatchResult doProcessRecord(String record) {
		BatchResult result = new GeneralBatchResult();
		try {
			String[] items = record.split("\\|");
			int i = 0;
			//读取文件字段
			<#list table.columnList as item>
			<#if item.iskey == false>
			<#if item.javaDataType == 'Date'>
			${item.javaDataType} ${item.columnName} = DateFormater.praseDate(items[i++]);
			<#elseif item.javaDataType == 'String'>
			${item.javaDataType} ${item.columnName} = items[i++];
			<#else>
			${item.javaDataType} ${item.columnName} = new ${item.javaDataType}(items[i++]);
			</#if>
			</#if>
			</#list>
			
			//新增记录
			${table.modelName} j = new ${table.modelName}();
			<#list table.keyList as item>
			<#if item.javaDataType == 'Long'>
			SequenceService ss = ConcurrentSequence.getInstance();
			j.set${item.columnName?cap_first}(ss.getSequence());
			<#else>
			SequenceService ss = TimeSequence.getInstance();
			j.set${item.columnName?cap_first}(ss.getSequence(""));
			</#if>
			</#list>
			<#list table.columnList as item>
			<#if item.iskey == false>
			j.set${item.columnName?cap_first}(${item.columnName});
			</#if>
			</#list>
			
			${table.modelName2}Service.doSave(j);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
}
