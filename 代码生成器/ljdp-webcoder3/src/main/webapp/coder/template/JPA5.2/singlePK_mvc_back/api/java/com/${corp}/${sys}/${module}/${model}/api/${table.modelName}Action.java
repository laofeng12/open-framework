<#if model == "">
package com.${corp}.${sys}.${module}.api;
<#else>
package com.${corp}.${sys}.${module}.${model}.api;
</#if>

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
import com.${corp}.${sys}.${module}.service.${table.modelName}Service;
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.query.${table.modelName}DBParam;
</#if>

<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
import com.${corp}.${sys}.${module}.${model}.service.${table.modelName}Service;
<#if baseFun.query == "on">
import com.${corp}.${sys}.${module}.${model}.query.${table.modelName}DBParam;
</#if>
</#if>

/**
 * api接口
 * @author ${author}
 *
 */
@Api(tags="${tableModelName}")
@RestController
<#if model == "">
@RequestMapping("/${sys}/${module}/${table.modelName2}")
<#else>
@RequestMapping("/${sys}/${module}/${model}/${table.modelName2}")
</#if>
public class ${table.modelName}Action {
	
	@Resource
	private ${table.modelName}Service ${table.modelName2}Service;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ${table.modelName} get(@PathVariable("id")${table.keyFieldType} id) {
		${table.modelName} m = ${table.modelName2}Service.get(id);
		return m;
	}
	
	<#if baseFun.query == "on">
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		<#list dbParamList as item>
		@ApiImplicitParam(name = "${item.condition}_${item.columnName}", value = "${item.name}${item.symbol}", required = false, dataType = "${item.javaDataType}", paramType = "query"),
		</#list>
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<${table.modelName}> doSearch(@ApiIgnore() ${table.modelName}DBParam params, @ApiIgnore() Pageable pageable){
		Page<${table.modelName}> result =  ${table.modelName2}Service.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	</#if>

	
	<#if baseFun.add == "on">
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody ${table.modelName} body
			<#if enableAttach==true>
			,@RequestParam(value="attachIds",required=false)String attachIds</#if>
			) {
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			<#if table.keyFieldType == "Long">
			SequenceService ss = ConcurrentSequence.getInstance();
			body.set${table.keyField?cap_first}(ss.getSequence());
			<#else>
			SequenceService ss = ConcurrentSequence.getInstance();
			body.set${table.keyField?cap_first}(ss.getSequence(""));
			</#if>
			body.setIsNew(true);//执行insert
			${table.modelName} dbObj = ${table.modelName2}Service.doSave(body);
		} else {
			//修改，记录更新时间等
			${table.modelName} db = ${table.modelName2}Service.get(body.get${table.keyField?cap_first}());
			<#list table.columnList as item>
			<#if item.iskey == false>
			set(body.get${item.columnName?cap_first}() != null, ()->db.set${item.columnName?cap_first}(body.get${item.columnName?cap_first}()));
			</#if>
			</#list>
			db.setIsNew(false);//执行update
			${table.modelName2}Service.doSave(db);
		}
		<#if enableAttach==true>
		if(StringUtils.isNotEmpty(attachIds)) {
			String[] attachItems = attachIds.split(",");
			for (String fid : attachItems) {
				//上传附件
				
			}
		}
		</#if>
		
		<#if enableAttach==true>
		</#if>
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}
	private void set(boolean condition, Runnable runnable) {
		if(condition) runnable.run();
	}
	</#if>
	
	<#if baseFun.delete == "on">
	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)${table.keyFieldType} id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			${table.modelName2}Service.doDelete(id);
		} else if(ids != null) {
			${table.modelName2}Service.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	</#if>
	
	<#if baseFun.export == "on">
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			${table.modelName}DBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<${table.modelName}> result = ${table.modelName2}Service.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
//			myBuilder.addProperty("useraccount", "账号");
//			myBuilder.addProperty("username", "用户姓名");
//			myBuilder.addProperty("creatime", "创建时间", FieldType.BASE_DATE, "yyyy-MM-dd");//设置时间格式
//			myBuilder.addProperty("userStatus", "用户状态", SysCodeUtil.codeToMap("sys.user.status"));//自动数据字典【tsys_code】翻译
//			Map<K, V> tfMap1 = new HashMap();
//			tfMap1.put(1, "状态1");
//			tfMap1.put(2, "状态2");
//			myBuilder.addProperty("userStatus", "用户状态",tfMap1);//写死静态字典翻译
			
			myBuilder.buildSheet("${resName}", result.getContent());//放到第一个sheet
			
			String filename = "${resName}("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
			response.setContentType(ContentType.EXCEL);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			//开始导出
			myBuilder.finish();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}
	</#if>
}
