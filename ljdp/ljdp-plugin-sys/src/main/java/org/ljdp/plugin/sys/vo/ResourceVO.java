package org.ljdp.plugin.sys.vo;

import java.util.ArrayList;
import java.util.List;

public class ResourceVO implements Comparable<ResourceVO>{
	private String resId;
	private String resName;
	private String resURL;
	private String resType;
	private int resSort;
	private String resIcon;
	private String parentResId;
	
	private List<ResourceVO> subResList;
	
	public ResourceVO() {
		
	}
	public ResourceVO(String resId, String resName, String resURL, String resIcon) {
		this.resId = resId;
		this.resName = resName;
		this.resURL = resURL;
		this.resIcon = resIcon;
	}
	public ResourceVO(String parentResId, String resId, String resName, String resURL, String resIcon) {
		this.parentResId = parentResId;
		this.resId = resId;
		this.resName = resName;
		this.resURL = resURL;
		this.resIcon = resIcon;
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getResURL() {
		return resURL;
	}
	public void setResURL(String resURL) {
		this.resURL = resURL;
	}
	public String getResType() {
		return resType;
	}
	public void setResType(String resType) {
		this.resType = resType;
	}
	public int getResSort() {
		return resSort;
	}
	public void setResSort(int resSort) {
		this.resSort = resSort;
	}
	public String getResIcon() {
		return resIcon;
	}
	public void setResIcon(String resIcon) {
		this.resIcon = resIcon;
	}
	@Override
	public int compareTo(ResourceVO o) {
		return this.resSort - o.resSort;
	}
	public String getParentResId() {
		return parentResId;
	}
	public void setParentResId(String parentResId) {
		this.parentResId = parentResId;
	}
	public List<ResourceVO> getSubResList() {
		return subResList;
	}
	public void setSubResList(List<ResourceVO> subResList) {
		this.subResList = subResList;
	}
	
	public void addSubResource(ResourceVO res) {
		if(subResList == null) {
			subResList = new ArrayList<>();
		}
		subResList.add(res);
	}
}
