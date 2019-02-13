package com.openjava.admin.logs.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class SysApiLogVO {

	@ApiModelProperty("接口请求ID")
	private String requestId;
	@ApiModelProperty("登陆帐号")
	private String account;
	@ApiModelProperty("登陆渠道")
	private String fromChannel;
	@ApiModelProperty("来源推广渠道")
	private String advertChannel;
	@ApiModelProperty("调用标识")
	private String requestIden;
	@ApiModelProperty("请求时间")
	private Date requestTime;
	@ApiModelProperty("响应时间")
	private Date responseTime;
	@ApiModelProperty("响应编码")
	private String responseCode;
	@ApiModelProperty("响应消息")
	private String responseMessage;
	@ApiModelProperty("调用方IP")
	private String clientIp;
	@ApiModelProperty("服务器IP")
	private String localIp;
	@ApiModelProperty("调用参数")
	private String requestParams;
	@ApiModelProperty("返回数据")
	private String responseData;
	@ApiModelProperty("客户端类型")
	private String userAgent;
	
	@ApiModelProperty("错误ID")
	private String errorId;
	@ApiModelProperty("错误信息")
	private String error;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getFromChannel() {
		return fromChannel;
	}
	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}
	public String getAdvertChannel() {
		return advertChannel;
	}
	public void setAdvertChannel(String advertChannel) {
		this.advertChannel = advertChannel;
	}
	public String getRequestIden() {
		return requestIden;
	}
	public void setRequestIden(String requestIden) {
		this.requestIden = requestIden;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}
