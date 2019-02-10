<#if model == "">
package com.${corp}.${sys}.${module}.controller;
<#else>
package com.${corp}.${sys}.${module}.${model}.controller;
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
import org.ljdp.common.bean.FieldType;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
 * 控制层
 * @author ${author}
 *
 */
@Controller
<#if model == "">
@RequestMapping("/${sys}/${module}/${table.modelName}")
<#else>
@RequestMapping("/${sys}/${module}/${model}/${table.modelName}")
</#if>
public class ${table.modelName}Controller {
	
	<#if buildPageJsp == true>
	<#if model == "">
	private final static String PAGE_EDIT = "${sys}/${module}/${table.modelName2}Edit";
	private final static String PAGE_QUERY = "${sys}/${module}/${table.modelName2}List";
	private final static String PAGE_SHOW = "${sys}/${module}/${table.modelName2}Show";
	private final static String PAGE_BATCH = "${sys}/${module}/${table.modelName2}Batch";
	<#else>
	private final static String PAGE_EDIT = "${sys}/${module}/${model}/${table.modelName2}Edit";
	private final static String PAGE_QUERY = "${sys}/${module}/${model}/${table.modelName2}List";
	private final static String PAGE_SHOW = "${sys}/${module}/${model}/${table.modelName2}Show";
	private final static String PAGE_BATCH = "${sys}/${module}/${model}/${table.modelName2}Batch";
	</#if>
	</#if>
	
	@Resource
	private ${table.modelName}Service ${table.modelName2}Service;

	<#if buildPageJsp == true>
	/**
	 * 转主页
	 * @resId: 菜单ID
	 */
	@RequestMapping("/mainpage.jspx")
	public ModelAndView mainPage(@RequestParam(value="resId",required=false)String resId){
		ModelAndView mav = new ModelAndView(PAGE_QUERY);
		
		return mav;
	}
	
	/**
	 * 转展示页面
	 */
	@RequestMapping(value="/show.jspx",method=RequestMethod.GET)
	public ModelAndView show(@RequestParam("id")String id) {
		ModelAndView mav = new ModelAndView(PAGE_SHOW);
		
		${table.modelName} m = ${table.modelName2}Service.get(id);
		mav.addObject("editAble", false);
		mav.addObject("m", m);
		return mav;
	}
	
	<#if baseFun.add == "on">
	/**
	 * 转新增页面
	 */
	@RequestMapping(value="/add.jspx",method=RequestMethod.GET)
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView(PAGE_EDIT);
		mav.addObject("isNew", true);
		mav.addObject("editAble", true);
		return mav;
	}
	</#if>
	
	/**
	 * 转编辑页面
	 */
	@RequestMapping(value="/edit.jspx",method=RequestMethod.GET)
	public ModelAndView edit(@RequestParam("id")String id) {
		ModelAndView mav = new ModelAndView(PAGE_EDIT);
		
		${table.modelName} m = ${table.modelName2}Service.get(id);
		mav.addObject("isNew", false);
		mav.addObject("editAble", true);
		mav.addObject("m", m);
		return mav;
	}
	
	<#if baseFun.importFun == "on">
	/**
	 * 转导入页面页面
	 */
	@RequestMapping(value="/batch.jspx",method=RequestMethod.GET)
	public ModelAndView batch() {
		ModelAndView mav = new ModelAndView(PAGE_BATCH);
		return mav;
	}
	</#if>
	</#if>
	
	<#if baseFun.query == "on">
	@RequestMapping(value="/search.act",method=RequestMethod.GET)
	@ResponseBody
	public TablePage<${table.modelName}> doSearch(${table.modelName}DBParam params, Pageable pageable){
		Page<${table.modelName}> result =  ${table.modelName2}Service.query(params, pageable);
		
		return new TablePageImpl<>(result.getTotalElements(), result.getContent());
	}
	
	</#if>

	
	<#if baseFun.add == "on">
	/**
	 * 保存
	 */
	@RequestMapping(value="/save.act", method=RequestMethod.POST)
	public ModelAndView doSave(${table.modelName} model, @RequestParam("isNew")Boolean isNew
			<#if enableAttach==true>
			,@RequestParam(value="attachIds",required=false)String attachIds</#if>
			) {
		if(isNew) {
			//新增，记录创建时间等
		} else {
			//修改，记录更新时间等
		}
		<#if enableAttach==true>
		
		</#if>
		${table.modelName} dbObj = ${table.modelName2}Service.doSave(model);
		<#if enableAttach==true>
		
		</#if>
		ModelAndView mav = new ModelAndView(PAGE_EDIT);
		mav.addObject(model);
		mav.addObject("success", true);
		return mav;
	}
	</#if>
	
	<#if baseFun.delete == "on">
	@RequestMapping(value="/delete.act",method=RequestMethod.POST)
	@ResponseBody
	public Result doDelete(@RequestParam("id")${table.keyFieldType} id) {
		${table.modelName2}Service.doDelete(id);
		Result res = new GeneralResult(true);
		return res;
	}
	</#if>
	
	<#if baseFun.export == "on">
	/**
	 * 导出Excel文件
	 */
	@RequestMapping(value="/export.act")
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			${table.modelName}DBParam params) throws Exception{
		try {
			Pageable pageable = new PageRequest(0, 20000);//限制只能导出2w，防止内存溢出
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
