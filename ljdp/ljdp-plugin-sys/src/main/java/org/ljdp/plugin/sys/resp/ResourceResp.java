package org.ljdp.plugin.sys.resp;

import java.util.List;

import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.plugin.sys.vo.MenuVO;
import org.ljdp.plugin.sys.vo.ResourceVO;

public class ResourceResp extends BasicApiResponse {
	private static final long serialVersionUID = 4749680421969052058L;
	
	private List<ResourceVO> resources;
	private List<String> resourceIds;
	private List<MenuVO> menus;

	public List<ResourceVO> getResources() {
		return resources;
	}

	public void setResources(List<ResourceVO> resources) {
		this.resources = resources;
	}

	public List<MenuVO> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuVO> menus) {
		this.menus = menus;
	}

	public List<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}
}
