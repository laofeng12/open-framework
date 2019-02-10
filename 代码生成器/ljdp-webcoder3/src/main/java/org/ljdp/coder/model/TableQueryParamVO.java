package org.ljdp.coder.model;

import org.ljdp.component.bean.BaseVO;


public class TableQueryParamVO extends BaseVO {

	private String code;
	private String name;
	
	private Boolean eq;
	private Boolean ne;
	private Boolean lt;
	private Boolean gt;
	private Boolean le;
	private Boolean ge;
	private Boolean like;
	private Boolean nlike;
	private Boolean in;
	private Boolean nin;
	private Boolean isnull;
	
	private String dictDefined;//数据字典定义
	private Boolean sort;//是否支持排序
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getEq() {
		return eq;
	}
	public void setEq(Boolean eq) {
		this.eq = eq;
	}
	public Boolean getNe() {
		return ne;
	}
	public void setNe(Boolean ne) {
		this.ne = ne;
	}
	public Boolean getLt() {
		return lt;
	}
	public void setLt(Boolean lt) {
		this.lt = lt;
	}
	public Boolean getGt() {
		return gt;
	}
	public void setGt(Boolean gt) {
		this.gt = gt;
	}
	public Boolean getLe() {
		return le;
	}
	public void setLe(Boolean le) {
		this.le = le;
	}
	public Boolean getGe() {
		return ge;
	}
	public void setGe(Boolean ge) {
		this.ge = ge;
	}
	public Boolean getLike() {
		return like;
	}
	public void setLike(Boolean like) {
		this.like = like;
	}
	public Boolean getNlike() {
		return nlike;
	}
	public void setNlike(Boolean nlike) {
		this.nlike = nlike;
	}
	public Boolean getIn() {
		return in;
	}
	public void setIn(Boolean in) {
		this.in = in;
	}
	public Boolean getNin() {
		return nin;
	}
	public void setNin(Boolean nin) {
		this.nin = nin;
	}
	public Boolean getIsnull() {
		return isnull;
	}
	public void setIsnull(Boolean isnull) {
		this.isnull = isnull;
	}
	public String getDictDefined() {
		return dictDefined;
	}
	public void setDictDefined(String dictDefined) {
		this.dictDefined = dictDefined;
	}
	public Boolean getSort() {
		return sort;
	}
	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	
}
