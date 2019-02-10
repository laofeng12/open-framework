package org.ljdp.ui.extjs;

import java.util.ArrayList;

public class TreeNode {
	private String id;
	private String text;
	private boolean leaf;
	private String iconCls;
	private String uri;
	private Boolean checked;
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	
	public TreeNode(String id, String text) {
		this.id = id;
		this.text = text;
		this.leaf = false;
	}
	
	public TreeNode(String id, String text, boolean leaf) {
		this.id = id;
		this.text = text;
		this.leaf = leaf;
	}
	
	public void appendChild(TreeNode node) {
		children.add(node);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Object getChecked() {
		if(checked != null) {			
			return checked;
		}
		return "";
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
