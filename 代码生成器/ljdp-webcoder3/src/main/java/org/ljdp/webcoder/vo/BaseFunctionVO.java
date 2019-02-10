package org.ljdp.webcoder.vo;

import org.ljdp.component.bean.BaseVO;

public class BaseFunctionVO extends BaseVO {

	public String mvc = "";
	public String mvc_api = "";
	public String mvc_web = "";
	
	public String query = "";
	public String add = "";
	public String modify = "";
	public String delete = "";
	public String importFun = "";
	public String export = "";
	
	public String mobilepage = "";
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getModify() {
		return modify;
	}
	public void setModify(String modify) {
		this.modify = modify;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	public String getImportFun() {
		return importFun;
	}
	public void setImportFun(String importFun) {
		this.importFun = importFun;
	}
	public String getExport() {
		return export;
	}
	public void setExport(String export) {
		this.export = export;
	}
	public String getMvc() {
		return mvc;
	}
	public void setMvc(String mvc) {
		this.mvc = mvc;
	}
	public String getMvc_api() {
		return mvc_api;
	}
	public void setMvc_api(String mvc_api) {
		this.mvc_api = mvc_api;
	}
	public String getMvc_web() {
		return mvc_web;
	}
	public void setMvc_web(String mvc_web) {
		this.mvc_web = mvc_web;
	}
	public String getMobilepage() {
		return mobilepage;
	}
	public void setMobilepage(String mobilepage) {
		this.mobilepage = mobilepage;
	}
}
