package com.openjava.framework.sys.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author hzy
 *
 */
@ApiModel("数据字典")
@Entity
@Table(name = "TSYS_CODE")
public class SysCode extends BasicApiResponse implements ApiResponse {
	private static final long serialVersionUID = -5155152631172811605L;
	
	private Integer codeid;//CODEID
	@ApiModelProperty("数据编码")
	private String codeDef;//CODE
	private String codetype;//CODETYPE
	@ApiModelProperty("数据名称")
	private String codename;//CODENAME
	private String codevalue;//CODEVALUE
	@ApiModelProperty("父编码")
	private Integer pcodeid;//PCODEID
	@ApiModelProperty("状态")
	private Integer status;
	@ApiModelProperty("图标")
	private String iconPath;
	
	@ApiModelProperty("排序")
	private String sort;
	
	@Id
	@Column(name = "CODEID")
	public Integer getCodeid() {
		return codeid;
	}
	public void setCodeid(Integer codeid) {
		this.codeid = codeid;
	}
	

	@Column(name = "CODE")
	public String getCodeDef() {
		return codeDef;
	}
	public void setCodeDef(String code) {
		this.codeDef = code;
	}

	@Column(name = "CODETYPE")
	public String getCodetype() {
		return codetype;
	}
	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	@Column(name = "CODENAME")
	public String getCodename() {
		return codename;
	}
	public void setCodename(String codename) {
		this.codename = codename;
	}

	@Column(name = "CODEVALUE")
	public String getCodevalue() {
		return codevalue;
	}
	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}

	@Column(name = "PCODEID")
	public Integer getPcodeid() {
		return pcodeid;
	}
	public void setPcodeid(Integer pcodeid) {
		this.pcodeid = pcodeid;
	}
	
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "ICON_PATH")
	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@Column(name = "SORT")
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}