package com.openjava.shop.log.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.log.model.RequestLog;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实体
 * @author hzy
 *
 */
@Entity
@Table(name = "LOG_API_REQUEST")
public class LogApiRequest extends BasicApiResponse implements ApiResponse, RequestLog {
	private static final long serialVersionUID = 6809395032349658777L;
	
	private Long id;//日志ID
	private String account;//登陆帐号
	private String fromChannel;//客户端渠道
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
	
	private String fromapp;//是否从app打开和app类型
	private String tub1;
	private String tub2;
	private String tub3;
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	

	@Column(name = "ACCOUNT")
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "FROM_CHANNEL")
	public String getFromChannel() {
		return fromChannel;
	}
	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	@Column(name = "REQUEST_IDEN")
	public String getRequestIden() {
		return requestIden;
	}
	public void setRequestIden(String requestIden) {
		this.requestIden = requestIden;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_TIME")
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESPONSE_TIME")
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "RESPONSE_MESSAGE")
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Column(name = "CLIENT_IP")
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	@Column(name = "LOCAL_IP")
	public String getLocalIp() {
		return localIp;
	}
	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	@Column(name = "REQUEST_PARAMS")
	public String getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	@Column(name = "RESPONSE_DATA")
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
	@Column(name = "ADVERT_CHANNEL")
	public String getAdvertChannel() {
		return advertChannel;
	}
	public void setAdvertChannel(String advertChannel) {
		this.advertChannel = advertChannel;
	}
	
	@Column(name = "TOKENID")
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	@Column(name = "USER_AGENT")
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	@Transient
	public String getFromapp() {
		return fromapp;
	}
	public void setFromapp(String fromapp) {
		this.fromapp = fromapp;
	}
	
	@Column(name = "TUB1")
	public String getTub1() {
		return tub1;
	}
	public void setTub1(String tub1) {
		this.tub1 = tub1;
	}
	@Column(name = "TUB2")
	public String getTub2() {
		return tub2;
	}
	public void setTub2(String tub2) {
		this.tub2 = tub2;
	}
	@Column(name = "TUB3")
	public String getTub3() {
		return tub3;
	}
	public void setTub3(String tub3) {
		this.tub3 = tub3;
	}
}