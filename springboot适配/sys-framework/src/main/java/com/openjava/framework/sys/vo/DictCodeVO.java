package com.openjava.framework.sys.vo;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.openjava.framework.sys.domain.SysCode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("数据字典")
public class DictCodeVO {
	@ApiModelProperty("数据编码")
	private String codeDef;//CODE
	@ApiModelProperty("数据名称")
	private String codename;//CODENAME
	@ApiModelProperty("父编码")
	private Integer pcodeid;//PCODEID
	@ApiModelProperty("图标")
	private String iconPath;
	
	public static DictCodeVO of(SysCode c) {
		DictCodeVO vo = new DictCodeVO();
		try {
			BeanUtils.copyProperties(vo, c);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public String getCodeDef() {
		return codeDef;
	}
	public void setCodeDef(String codeDef) {
		this.codeDef = codeDef;
	}
	public String getCodename() {
		return codename;
	}
	public void setCodename(String codename) {
		this.codename = codename;
	}
	public Integer getPcodeid() {
		return pcodeid;
	}
	public void setPcodeid(Integer pcodeid) {
		this.pcodeid = pcodeid;
	}

	public String getIconPath() {
		return iconPath;
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
}
