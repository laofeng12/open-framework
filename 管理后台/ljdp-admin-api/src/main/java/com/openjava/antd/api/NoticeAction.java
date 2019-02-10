package com.openjava.antd.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.secure.annotation.Security;
import org.ljdp.util.DateFormater;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.sys.domain.SysNotice;
import com.openjava.admin.sys.service.SysNoticeService;
import com.openjava.admin.sys.vo.AntdNotice;

/**
 * ant design pro 官方例子的接口实现
 * @author hzy0769
 *
 */
@RestController
public class NoticeAction {
	@Resource
	private SysNoticeService sysNoticeService;
	
	/**
	 * 通知接口
	 * @return
	 */
	@Security(session=true)
	@RequestMapping(value="/notices",method=RequestMethod.GET)
	public List<AntdNotice> notices() {
		List<SysNotice> notices = sysNoticeService.findMyNotice();
		
		List<AntdNotice> results = new ArrayList<>();
		notices.forEach(n -> {
			AntdNotice an = new AntdNotice();
			an.setId(n.getNid());
			an.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/GvqBnKhFgObvnSGkDsje.png");
			an.setTitle(n.getTitle());
			an.setDatetime(DateFormater.formatDatetime(n.getUpdateTime()));
			an.setType("通知");
			results.add(an);
		});
		return results;
	}
}
