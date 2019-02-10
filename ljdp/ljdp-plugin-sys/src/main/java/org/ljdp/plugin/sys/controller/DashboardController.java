package org.ljdp.plugin.sys.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.plugin.sys.dict.SysConstants;
import org.ljdp.plugin.sys.resp.ResourceResp;
import org.ljdp.plugin.sys.resp.SystemResp;
import org.ljdp.plugin.sys.service.LjdpResService;
import org.ljdp.plugin.sys.vo.ResourceVO;
import org.ljdp.plugin.sys.vo.RoleVO;
import org.ljdp.plugin.sys.vo.SystemVO;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	private final static String PAGE_INDEX = "dashboard/index";
	
	@RequestMapping(value="/index.jspx",method=RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		UserVO user = (UserVO)SsoContext.getSignedInUserFromSession(request);
		RoleVO role;
		if(user.getRoleList() != null && !user.getRoleList().isEmpty()) {
			role = user.getRoleList().get(0);
		} else {
			role = new RoleVO("-1", "无角色");
		}
		
		LjdpResService resService = (LjdpResService)SpringContextManager.getBean(SysConstants.RES_SERVICE_BEAN);
		ResourceResp resResp = resService.findByUser(user);
		List<ResourceVO> resList = new ArrayList<>();
		if(resResp.getCode().intValue() == APIConstants.CODE_SUCCESS) {
			resList = resResp.getResources();
		} else {
			System.out.println("获取用户菜单失败："+resResp.getCode()+","+resResp.getMessage());
		}
		
		String title = "后台管理";
		SystemResp sysResp = resService.findMySystem();
		if(sysResp.getCode().intValue() == APIConstants.CODE_SUCCESS) {
			List<SystemVO> systems = sysResp.getSystems();
			if(systems != null && !systems.isEmpty()) {
				SystemVO s = systems.get(0);
				title = s.getSysName();
			}
		}
		
		ModelAndView mav = new ModelAndView(PAGE_INDEX);
		mav.addObject("u", user);
		mav.addObject("r", role);
		mav.addObject("resList", resList);
		mav.addObject("title", title);
		mav.addObject("logoutURL", "/dashboard/user/logout.act");
		return mav;
	}
}
