package org.ljdp.plugin.sys.resp;

import java.util.List;

import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.plugin.sys.vo.SystemVO;

public class SystemResp extends BasicApiResponse {
	private static final long serialVersionUID = 6369053975219421017L;
	
	private List<SystemVO> systems;

	public List<SystemVO> getSystems() {
		return systems;
	}

	public void setSystems(List<SystemVO> systems) {
		this.systems = systems;
	}
}
