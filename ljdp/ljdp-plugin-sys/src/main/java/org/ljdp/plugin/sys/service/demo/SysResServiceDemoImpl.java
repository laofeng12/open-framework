package org.ljdp.plugin.sys.service.demo;

import java.util.ArrayList;
import java.util.List;

import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.resp.ResourceResp;
import org.ljdp.plugin.sys.resp.SystemResp;
import org.ljdp.plugin.sys.service.LjdpResService;
import org.ljdp.plugin.sys.vo.ResourceVO;
import org.ljdp.plugin.sys.vo.SystemVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 演示服务，不连数据库，返回一些测试数据
 * @author hzy
 *
 */
@Transactional
public class SysResServiceDemoImpl implements LjdpResService {

	@Override
	public ResourceResp findByUser(BaseUserInfo user) {
		String testUrl = "ljdp3/fda/cert/FdaCertPrintSummary/mainpage.jspx";
		ResourceVO r1 = new ResourceVO("1", "主页", "", "fa fa-home");
		r1.addSubResource(new ResourceVO("11", "主页示例1", testUrl, ""));
		r1.addSubResource(new ResourceVO("12", "主页示例2", testUrl, ""));
		
		ResourceVO r2 = new ResourceVO("2", "布局", testUrl, "fa fa-columns");
		
		ResourceVO r3 = new ResourceVO("3", "统计图表", "", "fa fa-bar-chart-o");
		r3.addSubResource(new ResourceVO("31", "统计图表1", testUrl, ""));
		r3.addSubResource(new ResourceVO("32", "统计图表2", testUrl, ""));
		
		List<ResourceVO> mylist = new ArrayList<>();
		mylist.add(r1);
		mylist.add(r2);
		mylist.add(r3);
		
		ResourceResp resp = new ResourceResp();
		resp.setResources(mylist);
		return resp;
	}

	@Override
	public SystemResp findMySystem() {
		List<SystemVO> systems = new ArrayList<>();
		systems.add(new SystemVO("0", "演示后台管理"));
		SystemResp resp = new SystemResp();
		resp.setSystems(systems);
		return resp;
	}
}
