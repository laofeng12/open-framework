package org.ljdp.component.user;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户信息")
public interface BaseUserInfo {

	@ApiModelProperty("用户id")
	public String getUserId();

	public void setUserId(String userId);

	@ApiModelProperty("账号")
	public String getUserAccount();

	public void setUserAccount(String userAccount);

	@ApiModelProperty("名称")
	public String getUserName();

	public void setUserName(String userName);

	public String getUserPwd();

	public void setUserPwd(String userPwd);

	@ApiModelProperty("手机号码")
	public String getUserMobileno();

	public void setUserMobileno(String userMobileno);

	@ApiModelProperty("邮箱")
	public String getUserEmail();

	public void setUserEmail(String userEmail);

	@ApiModelProperty("备注")
	public String getUserRemark();

	public void setUserRemark(String userRemark);

	@ApiModelProperty("用户类型")
	public String getUserType();

	public void setUserType(String userType);

	@ApiModelProperty("注册方式")
	public String getRegisterWay();

	public void setRegisterWay(String registerWay);

	public String getTokenId();

	public void setTokenId(String tokenId);

	@ApiModelProperty("头像")
	public String getHeadImg();

	public void setHeadImg(String headImg);

	@ApiModelProperty("登录时间")
	public Date getLoginTime();

	public void setLoginTime(Date loginTime);

	public String getUserAgent();

	public void setUserAgent(String userAgent);

	@ApiModelProperty("所属部门ids")
	public List<Long> getOrgIds();

	public void setOrgIds(List<Long> orgIds);

	@ApiModelProperty("所属部门编码")
	public List<String> getOrgCodes();

	public void setOrgCodes(List<String> orgCodes);

	@ApiModelProperty("所属商家ID")
	public Long getShopId();

	public void setShopId(Long shopId);

	@ApiModelProperty("所属商家编码")
	public String getShopCode();

	public void setShopCode(String shopCode);

	@ApiModelProperty("当前登录的平台")
	public String getPlatform();

	public void setPlatform(String platform);

	@ApiModelProperty("用户编码")
	public String getUserCode();

	public void setUserCode(String userCode);

	@ApiModelProperty("所属商家名称")
	public String getShopName();

	public void setShopName(String shopName);
	
	@ApiModelProperty("角色权限别名列表")
	public List<String> getRoleAlias();
	
	public void setRoleAlias(List<String> roleAlias);
	
	@ApiModelProperty("用户编号")
	public String getUserNo();

	public void setUserNo(String userNo);

	@ApiModelProperty("是否管理员")
	public Boolean getIsManager();

	public void setIsManager(Boolean isManager);

	public Date getRefreshTime();

	public void setRefreshTime(Date refreshTime);

	public Integer getExpireInMin();

	public void setExpireInMin(Integer expireInMin);
}