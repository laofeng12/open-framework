package com.openjava.admin.logs.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author 子右
 *
 */
@ApiModel("请求日志")
@Entity
@Table(name = "LOG_API_REQUEST")
public class SysLogApiRequest implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("日志ID")
	private Long id;
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
	@ApiModelProperty("TOKENID")
	private String tokenid;
	@ApiModelProperty("客户端类型")
	private String userAgent;
	@ApiModelProperty("用户行为跟踪（页面id）")
	private String tub1;
	@ApiModelProperty("用户行为跟踪（页面区域）")
	private String tub2;
	@ApiModelProperty("用户行为跟踪（点击单元）")
	private String tub3;
	
	@ApiModelProperty("是否新增")
    private Boolean isNew;
	
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.id != null) {
    		return false;
    	}
    	return true;
    }
    
    @Transient
    public Boolean getIsNew() {
		return isNew;
	}
    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }
	
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
	

	@Column(name = "ADVERT_CHANNEL")
	public String getAdvertChannel() {
		return advertChannel;
	}
	public void setAdvertChannel(String advertChannel) {
		this.advertChannel = advertChannel;
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
	

	@Column(name = "TOKENID")
	public String getTokenid() {
		return tokenid;
	}
	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}
	

	@Column(name = "USER_AGENT")
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
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