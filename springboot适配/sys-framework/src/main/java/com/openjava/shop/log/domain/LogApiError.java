package com.openjava.shop.log.domain;

import java.util.Date;

import javax.persistence.*;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.log.model.RequestErrorLog;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实体
 * @author hzy
 *
 */
@Entity
@Table(name = "LOG_API_ERROR")
public class LogApiError extends BasicApiResponse implements ApiResponse, RequestErrorLog {
	private static final long serialVersionUID = 1167473729782116581L;
	
	private String errorId;//错误ID
	private String requestId;//接口请求ID
	private String error;//错误信息
	private Date errorDate;//错误时间
	
	//联表查
	private String account;//登陆帐号
	private String fromChannel;//登陆渠道
	private String advertChannel;//推广渠道
	private String requestIden;//调用标识
	private Date requestTime;//登陆时间
	private Date responseTime;//响应时间
	private String responseCode;//响应编码
	private String responseMessage;//响应消息
	private String clientIp;//调用方IP
	private String localIp;//服务器IP
	private String requestParams;//REQUEST_PARAMS
	private String responseData;//RESPONSE_DATA
	private String tokenId;
	private String userAgent;
	
	
	
	
	@Id
	@Column(name = "ERROR_ID")
	public String getErrorId() {
		return errorId;
	}
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
	

	@Column(name = "REQUEST_ID")
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	

	@Column(name = "ERROR")
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ERROR_DATE")
	public Date getErrorDate() {
		return errorDate;
	}
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}
	
	@Transient
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Transient
	public String getFromChannel() {
		return fromChannel;
	}
	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}
	@Transient
	public String getAdvertChannel() {
		return advertChannel;
	}
	public void setAdvertChannel(String advertChannel) {
		this.advertChannel = advertChannel;
	}
	@Transient
	public String getRequestIden() {
		return requestIden;
	}
	public void setRequestIden(String requestIden) {
		this.requestIden = requestIden;
	}
	@Transient
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	@Transient
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	@Transient
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	@Transient
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Transient
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	@Transient
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}
	@Transient
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	@Transient
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	@Transient
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	@Transient
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
}