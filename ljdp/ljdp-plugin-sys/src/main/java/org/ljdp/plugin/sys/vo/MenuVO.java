package org.ljdp.plugin.sys.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuVO {
	private String name;
	private String icon;
	private String path;
	private List<MenuVO> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<MenuVO> getChildren() {
		return children;
	}
	public void setChildren(List<MenuVO> children) {
		this.children = children;
	}
	
	public static MenuVO fromResources(ResourceVO r) {
		MenuVO m = new MenuVO();
		m.setName(r.getResName());
		m.setIcon(r.getResIcon());
		m.setPath(r.getResURL());
		if(r.getSubResList() != null) {
			List<MenuVO> children = new ArrayList<>(r.getSubResList().size());
			r.getSubResList().forEach(sr -> {
				MenuVO cm = MenuVO.fromResources(sr);
				children.add(cm);
			});
			m.setChildren(children);
		}
		return m;
	}
	
	public static void toResourceIds(List<String> resourceIds, ResourceVO r) {
		resourceIds.add(r.getResId());
		if(r.getSubResList() != null) {
			r.getSubResList().forEach(sr -> {
				toResourceIds(resourceIds, sr);
			});
		}
	}
}
