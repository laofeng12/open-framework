package org.ljdp.support.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.support.attach.component.HwcloudConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="华为云")
@Controller
@RequestMapping("/ljdp/sdkconfig")
public class SdkConfigAction {
	@Resource
	private HwcloudConfig config;
	
	@ApiOperation(value = "获取华为云OBS配置", notes = "单个对象查询",nickname="obsConfig")
	@RequestMapping(value="/obsConfig")
	public HwcloudConfig obsConfig(HttpServletRequest request, 
			HttpServletResponse response) {
		return config;
	}
}
