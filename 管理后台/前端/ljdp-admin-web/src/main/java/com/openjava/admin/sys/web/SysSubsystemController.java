package com.openjava.admin.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.ui.spring.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.openjava.admin.sys.domain.SysSubsystem;

/**
 * 前端页面控制层
 * @author hzy
 *
 */
@Controller
@RequestMapping("/admin/sys/sysSubsystem")
public class SysSubsystemController extends BaseController{
	
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
		
		SysSubsystem m = loadFromApi(request, id);
		mav.addObject("editAble", false);
		mav.addObject("m", m);
		return mav;
	}
	
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
	
	@RequestMapping(value="/edit.ht",params="method=edit",method=RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @RequestParam("id")Long id) throws Exception{
		ModelAndView mav = super.getAutoView();
		
		SysSubsystem m = loadFromApi(request, id);
		mav.addObject("isNew", false);
		mav.addObject("editAble", true);
		mav.addObject("m", m);
		return mav;
	}
	
	
	private SysSubsystem loadFromApi(final HttpServletRequest request, final Long id) throws Exception {
		ApiRequestEntity apientity = new ApiRequestEntity(request);
		String apiUrl = "/api/admin/sys/sysSubsystem/"+id;
		apientity.setUrl(apiUrl);
		SysSubsystem m = ApiClient.doGet(apientity, SysSubsystem.class);
		return m;
	}
}
