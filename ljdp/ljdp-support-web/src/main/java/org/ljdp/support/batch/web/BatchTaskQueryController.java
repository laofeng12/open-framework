package org.ljdp.support.batch.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page/filebatch")
public class BatchTaskQueryController {
	private final static String PAGE_QUERY = "/ljdp/batch/batchFileimportTaskList";

	/**
	 * 转主页
	 * @resId: 菜单ID
	 */
	@RequestMapping("/list.jspx")
	public ModelAndView mainPage(@RequestParam(value="resId",required=false)String resId) {
		ModelAndView mav = new ModelAndView(PAGE_QUERY);
		
		return mav;
	}
	
}
