package org.ljdp.plugin.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.dict.SysConstants;
import org.ljdp.plugin.sys.resp.LoginResp;
import org.ljdp.plugin.sys.service.LjdpUserService;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard/user")
public class UserLoginController {
	private final static String PAGE_LOGIN = "login";
	
	@RequestMapping(value="/login.jspx",method=RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView(PAGE_LOGIN);
		return mav;
	}
	
	@RequestMapping(value="/login.act",method=RequestMethod.POST)
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("userAccount")String userAccount
			,@RequestParam("userPwd")String userPwd
			,@RequestParam("authCode")String authCode) {
		LjdpUserService service = (LjdpUserService)SpringContextManager.getBean(SysConstants.USER_SERVICE_BEAN);
		LoginResp resp = service.findUserByAccountAndPwd(userAccount, userPwd);
		if(resp.getCode().intValue() == APIConstants.CODE_SUCCESS) {
			BaseUserInfo user = resp.getUser();
			if(user != null) {
				SsoContext.signIn(request, user, user.getTokenId());
				ModelAndView mav = new ModelAndView("redirect:/dashboard/index.jspx");
				return mav;
			} else {
				ModelAndView mav = new ModelAndView(PAGE_LOGIN);
				mav.addObject("errMsg", "登录失败user is null");
				return mav;
			}
		} else {
			ModelAndView mav = new ModelAndView(PAGE_LOGIN);
			mav.addObject("errMsg", resp.getMessage());
			return mav;
		}
	}
	
	@RequestMapping(value="/logout.act",method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		SsoContext.singOut(request);
		ModelAndView mav = new ModelAndView("redirect:/dashboard/user/login.jspx");
		return mav;
	}
	
}
