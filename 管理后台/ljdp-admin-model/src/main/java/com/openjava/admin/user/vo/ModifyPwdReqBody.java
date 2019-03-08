package com.openjava.admin.user.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("修改密码参数")
public class ModifyPwdReqBody {

	@ApiModelProperty(value="新密码",required = true)
	@NotNull
    private String oldpassword;

    @ApiModelProperty(value="新密码",required = true)
    @NotNull
    private String newpassword;

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

}
