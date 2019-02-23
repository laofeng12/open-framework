package com.openjava.admin.res.web;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.ui.spring.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.res.domain.SysRes;

/**
 * 前端页面控制层
 * @author hzy
 *
 */
@Controller
@RequestMapping("/admin/res/sysRes")
public class SysResController extends BaseController{
	
	/**
	 * 菜单对应角色权限管理
	 * @return
	 */
	@RequestMapping(value="/roleTree.ht", method=RequestMethod.GET)
	public ModelAndView roleTree(@RequestParam("roleid")Long roleid) {
		ModelAndView mav = super.getAutoView();
		mav.addObject("roleid", roleid);
		return mav;
	}
	
	/**
	 * 菜单管理
	 * @param resId
	 * @return
	 */
	@RequestMapping(value="/tree.ht", method=RequestMethod.GET)
	public ModelAndView tree(@RequestParam(value="resId",required=false)String resId) {
		ModelAndView mav = super.getAutoView();
		
		return mav;
	}
	
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
	public ModelAndView show(HttpServletRequest request, @RequestParam("id")Long id) throws Exception{
		ModelAndView mav = super.getAutoView();
		
		SysRes m = loadFromApi(request, id);
		mav.addObject("editAble", false);
		mav.addObject("m", m);
		return mav;
	}
	
	/**
	 * 转新增页面
	 */
	@RequestMapping(value="/edit.ht",params="method=add",method=RequestMethod.GET)
	public ModelAndView add(@RequestParam("parentid")Long parentid) {
		ModelAndView mav = super.getAutoView();
		mav.addObject("isNew", true);
		mav.addObject("editAble", true);
		mav.addObject("title", "菜单管理-添加");
		SysRes m = new SysRes();
		m.setParentid(parentid);
		mav.addObject("m", m);
		return mav;
	}
	
	@RequestMapping(value="/edit.ht",params="method=edit",method=RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @RequestParam("id")Long id) throws Exception{
		ModelAndView mav = super.getAutoView();
		
		SysRes m = loadFromApi(request, id);
		mav.addObject("isNew", false);
		mav.addObject("editAble", true);
		mav.addObject("m", m);
		mav.addObject("title", "菜单管理-编辑");
		return mav;
	}
	
	
	private SysRes loadFromApi(final HttpServletRequest request, final Long id) throws Exception {
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		String apiUrl = "/api/admin/res/sysRes/"+id;
		apientity.setUrl(apiUrl);
		SysRes m = ApiClient.doGet(apientity, SysRes.class);
		return m;
	}
}
