package org.ljdp.plugin.sys.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.user.BaseUserInfo;

public class UserVO implements Serializable, BaseUserInfo{
	private static final long serialVersionUID = -2921703610045899709L;
	
	private String tokenId;
	private String userId;
	private String userCode;
	private String userAccount;
	private String userName;
	private String userPwd;
	private String userMobileno;
	private String userEmail;
	private String userRemark;
	private String userType;
	private String registerWay;//注册方式
	private String headImg;//头像
	private Date loginTime;//登录时间
	private Date refreshTime;//token刷新时间
	private Integer expireInMin;//token过期时间(分)
	private String userAgent;
	private List<RoleVO> roleList = new ArrayList<>();
	private List<OrgVO> orgList = new ArrayList<>();
	private List<Long> orgIds = new ArrayList<>();
	private List<String> orgCodes = new ArrayList<>();
	private List<String> roleAlias = new ArrayList<>();
	private String userNo;
	private Boolean isManager;
	
	// 给第三方
	private Long shopId;
	private String shopCode;
	private String shopName;
	private String platform;//访问的平台
	
	public UserVO() {
		
	}
	
	public UserVO(String userId, String userAccount, String userName) {
		this.userId = userId;
		this.userAccount = userAccount;
		this.userName = userName;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserAccount()
	 */
	@Override
	public String getUserAccount() {
		return userAccount;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserAccount(java.lang.String)
	 */
	@Override
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserName()
	 */
	@Override
	public String getUserName() {
		return userName;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserName(java.lang.String)
	 */
	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserPwd()
	 */
	@Override
	public String getUserPwd() {
		return userPwd;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserPwd(java.lang.String)
	 */
	@Override
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserMobileno()
	 */
	@Override
	public String getUserMobileno() {
		return userMobileno;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserMobileno(java.lang.String)
	 */
	@Override
	public void setUserMobileno(String userMobileno) {
		this.userMobileno = userMobileno;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserEmail()
	 */
	@Override
	public String getUserEmail() {
		return userEmail;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserEmail(java.lang.String)
	 */
	@Override
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserRemark()
	 */
	@Override
	public String getUserRemark() {
		return userRemark;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserRemark(java.lang.String)
	 */
	@Override
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserType()
	 */
	@Override
	public String getUserType() {
		return userType;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserType(java.lang.String)
	 */
	@Override
	public void setUserType(String userType) {
		this.userType = userType;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getRegisterWay()
	 */
	@Override
	public String getRegisterWay() {
		return registerWay;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setRegisterWay(java.lang.String)
	 */
	@Override
	public void setRegisterWay(String registerWay) {
		this.registerWay = registerWay;
	}
	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getRoleList()
	 */
	public List<RoleVO> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleVO> roleList) {
		this.roleList = roleList;
		
		roleList.forEach(r -> {
			if(StringUtils.isNotBlank(r.getRoleAlias())) {
				roleAlias.add(r.getRoleAlias());
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getTokenId()
	 */
	@Override
	public String getTokenId() {
		return tokenId;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setTokenId(java.lang.String)
	 */
	@Override
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getHeadImg()
	 */
	@Override
	public String getHeadImg() {
		return headImg;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setHeadImg(java.lang.String)
	 */
	@Override
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getLoginTime()
	 */
	@Override
	public Date getLoginTime() {
		return loginTime;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setLoginTime(java.util.Date)
	 */
	@Override
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserAgent()
	 */
	@Override
	public String getUserAgent() {
		return userAgent;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserAgent(java.lang.String)
	 */
	@Override
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getOrgIds()
	 */
	@Override
	public List<Long> getOrgIds() {
		return orgIds;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setOrgIds(java.util.List)
	 */
	@Override
	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getOrgCodes()
	 */
	@Override
	public List<String> getOrgCodes() {
		return orgCodes;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setOrgCodes(java.util.List)
	 */
	@Override
	public void setOrgCodes(List<String> orgCodes) {
		this.orgCodes = orgCodes;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getOrgList()
	 */
	public List<OrgVO> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<OrgVO> orgList) {
		this.orgList = orgList;
		
		orgIds.clear();
		orgList.forEach(o -> {
			orgIds.add(o.getOrgId());
		});
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getShopId()
	 */
	@Override
	public Long getShopId() {
		return shopId;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setShopId(java.lang.Long)
	 */
	@Override
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getShopCode()
	 */
	@Override
	public String getShopCode() {
		return shopCode;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setShopCode(java.lang.String)
	 */
	@Override
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getPlatform()
	 */
	@Override
	public String getPlatform() {
		return platform;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setPlatform(java.lang.String)
	 */
	@Override
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getUserCode()
	 */
	@Override
	public String getUserCode() {
		return userCode;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setUserCode(java.lang.String)
	 */
	@Override
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#getShopName()
	 */
	@Override
	public String getShopName() {
		return shopName;
	}

	/* (non-Javadoc)
	 * @see org.ljdp.plugin.sys.vo.BaseUserInfo#setShopName(java.lang.String)
	 */
	@Override
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<String> getRoleAlias() {
		return roleAlias;
	}

	public void setRoleAlias(List<String> roleAlias) {
		this.roleAlias = roleAlias;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Boolean getIsManager() {
		return isManager;
	}

	public void setIsManager(Boolean isManager) {
		this.isManager = isManager;
	}

	public Date getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(Date refreshTime) {
		this.refreshTime = refreshTime;
	}

	public Integer getExpireInMin() {
		return expireInMin;
	}

	public void setExpireInMin(Integer expireInMin) {
		this.expireInMin = expireInMin;
	}

	
}
