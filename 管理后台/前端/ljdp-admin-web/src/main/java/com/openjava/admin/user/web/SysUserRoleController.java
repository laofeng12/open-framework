package com.openjava.admin.user.web;

import javax.servlet.http.HttpServletRequest;

import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.ui.spring.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.openjava.admin.user.domain.SysUserRole;

/**
 * 前端页面控制层
 * @author hzy
 *
 */
@Controller
@RequestMapping("/admin/user/sysUserRole")
public class SysUserRoleController extends BaseController{
	
	/**
	 * 转主页
	 * @resId: 菜单ID
	 */
	@RequestMapping(value="/list.ht", method=RequestMethod.GET)
	public ModelAndView list(@RequestParam(value="resId",required=false)String resId,
			@RequestParam(value="userid")String userid) {
		ModelAndView mav = super.getAutoView();
		mav.addObject("userid", userid);
		return mav;
	}
	
}
