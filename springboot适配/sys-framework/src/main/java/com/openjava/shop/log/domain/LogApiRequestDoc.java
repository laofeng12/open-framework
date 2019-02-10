package com.openjava.shop.log.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class LogApiRequestDoc implements Serializable {
	private static final long serialVersionUID = 2048213845663072168L;
//	@Id
//	private String id;
	@Id
	private Long logid;//日志ID
	@Field
	private String account;//登陆帐号
	@Field
	private String fromChannel;//登陆渠道
	@Field
	private String advertChannel;//推广渠道
	@Field
	private String requestIden;//调用标识
	@Field
	private Date requestTime;//登陆时间
	@Field
	private Date responseTime;//响应时间
	@Field
	private String responseCode;//响应编码
	@Field
	private String responseMessage;//响应消息
	@Field
	private String clientIp;//调用方IP
	@Field
	private String localIp;//服务器IP
	@Field
	private String requestParams;//REQUEST_PARAMS
	@Field
	private String responseData;//RESPONSE_DATA
	@Field
	private String tokenId;
	@Field
	private String userAgent;
	@Field
	private String fromapp;//是否从app打开和app类型
	@Field
	private String tub1;
	@Field
	private String tub2;
	@Field
	private String tub3;
	
	public Long getLogid() {
		return logid;
	}
	public void setLogid(Long logid) {
		this.logid = logid;
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
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getFromapp() {
		return fromapp;
	}
	public void setFromapp(String fromapp) {
		this.fromapp = fromapp;
	}
	public String getTub1() {
		return tub1;
	}
	public void setTub1(String tub1) {
		this.tub1 = tub1;
	}
	public String getTub2() {
		return tub2;
	}
	public void setTub2(String tub2) {
		this.tub2 = tub2;
	}
	public String getTub3() {
		return tub3;
	}
	public void setTub3(String tub3) {
		this.tub3 = tub3;
	}
}
