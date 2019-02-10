package org.ljdp.plugin.sys.service;

import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.resp.ResourceResp;
import org.ljdp.plugin.sys.resp.SystemResp;

public interface LjdpResService {

	public ResourceResp findByUser(BaseUserInfo user);
	
	/**
	 * 获取我可以访问的系统列表
	 * @return
	 */
	public SystemResp findMySystem();
}
