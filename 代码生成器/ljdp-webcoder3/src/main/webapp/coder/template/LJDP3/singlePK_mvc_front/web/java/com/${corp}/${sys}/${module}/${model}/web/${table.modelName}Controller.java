<#if model == "">
package com.${corp}.${sys}.${module}.web;
<#else>
package com.${corp}.${sys}.${module}.${model}.web;
</#if>

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.ui.spring.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

<#if model == "">
import com.${corp}.${sys}.${module}.domain.${table.modelName};
<#else>
import com.${corp}.${sys}.${module}.${model}.domain.${table.modelName};
</#if>

/**
 * 前端页面控制层
 * @author ${author}
 *
 */
@Controller
<#if model == "">
@RequestMapping("/${sys}/${module}/${table.modelName2}")
<#else>
@RequestMapping("/${sys}/${module}/${model}/${table.modelName2}")
</#if>
public class ${table.modelName}Controller extends BaseController{
	
	/**
	 * 转主页
	 * @resId: 菜单ID
	 */
	@RequestMapping(value="/list.ht", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(value="resId",required=false)String resId) {
		ModelAndView mav = super.getAutoView();
		
		return mav;
	}
	
	/**
	 * 转展示页面
	 */
	@RequestMapping(value="/show.ht",method=RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request, @RequestParam("id")${table.keyFieldType} id) throws Exception{
		ModelAndView mav = super.getAutoView();
		
		${table.modelName} m = loadFromApi(request, id);
		mav.addObject("editAble", false);
		mav.addObject("m", m);
		return mav;
	}
	
	<#if baseFun.add == "on">
	/**
	 * 转新增页面
	 */
	@RequestMapping(value="/edit.ht",params="method=add",method=RequestMethod.GET)
	public ModelAndView add() {
		ModelAndView mav = super.getAutoView();
		mav.addObject("isNew", true);
		mav.addObject("editAble", true);
		return mav;
	}
	</#if>
	
	@RequestMapping(value="/edit.ht",params="method=edit",method=RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @RequestParam("id")${table.keyFieldType} id) throws Exception{
		ModelAndView mav = super.getAutoView();
		
		${table.modelName} m = loadFromApi(request, id);
		mav.addObject("isNew", false);
		mav.addObject("editAble", true);
		mav.addObject("m", m);
		return mav;
	}
	
	<#if baseFun.importFun == "on">
	/**
	 * 转导入页面页面
	 */
	@RequestMapping(value="/batch.ht",method=RequestMethod.GET)
	public ModelAndView batch() {
		ModelAndView mav = super.getAutoView();
		return mav;
	}
	</#if>
	
	private ${table.modelName} loadFromApi(final HttpServletRequest request, final ${table.keyFieldType} id) throws Exception {
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		<#if model == "">
		String apiUrl = "/api/${sys}/${module}/${table.modelName2}/"+id;
		<#else>
		String apiUrl = "/api/${sys}/${module}/${model}/${table.modelName2}/"+id;
		</#if>
		apientity.setUrl(apiUrl);
		${table.modelName} m = ApiClient.doGet(apientity, ${table.modelName}.class);
		return m;
	}
}
