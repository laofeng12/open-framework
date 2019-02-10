package com.openjava.tree;

public class ZTreeNode {

	private String id;
	private String name;
	private String pId;
	private Boolean open;
	private Boolean drag;
	private Integer sort;
	private String refId;
	private Boolean checked;
	private Boolean nocheck;
	
	public ZTreeNode() {
		
	}
	
	public ZTreeNode(String id, String name, String pId) {
		super();
		this.id = id;
		this.name = name;
		this.pId = pId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getDrag() {
		return drag;
	}

	public void setDrag(Boolean drag) {
		this.drag = drag;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}
}
