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
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
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
@Api(tags="${resName}")
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
	@ApiOperation(value = "保存", nickname="保存")
	@ApiImplicitParams({
		<#list table.columnList as item>
		@ApiImplicitParam(name = "${item.columnName}", value = "${item.comment}", required = false, dataType = "${item.javaDataType}", paramType = "post"),
		</#list>
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(@ApiIgnore() ${table.modelName} model, @RequestParam("isNew")Boolean isNew
			<#if enableAttach==true>
			,@RequestParam(value="attachIds",required=false)String attachIds</#if>
			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			<#if table.keyFieldType == "Long">
			SequenceService ss = ConcurrentSequence.getInstance();
			model.set${table.keyField?cap_first}(ss.getSequence());
			<#else>
			SequenceService ss = ConcurrentSequence.getInstance();
			model.set${table.keyField?cap_first}(ss.getSequence(""));
			</#if>
			${table.modelName} dbObj = ${table.modelName2}Service.doSave(model);
		} else {
			//修改，记录更新时间等
			${table.modelName} db = ${table.modelName2}Service.get(model.get${table.keyField?cap_first}());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
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
		BasicApiResponse resp = new BasicApiResponse();
		return resp;
	}
	</#if>
	
	<#if baseFun.delete == "on">
	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")${table.keyFieldType} id) {
		${table.modelName2}Service.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "批量删除", nickname="remove")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public ApiResponse doRemove(@RequestParam("ids")String ids) {
		${table.modelName2}Service.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
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
