package com.openjava.tree;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("antd树节点")
public class AntdTreeNode {

	private String key;
	private String title;
	private String parentKey;
	private List<AntdTreeNode> children;
	@ApiModelProperty("是否勾选")
	private Boolean checked;
	
	public AntdTreeNode(String key, String title, String parentKey) {
		super();
		this.key = key;
		this.title = title;
		this.parentKey = parentKey;
		children = new ArrayList<>();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	public List<AntdTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<AntdTreeNode> children) {
		this.children = children;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}
