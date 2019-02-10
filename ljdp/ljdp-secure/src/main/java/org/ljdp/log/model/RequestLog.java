package org.ljdp.log.model;

import java.util.Date;

public interface RequestLog extends java.io.Serializable{

	public Long getId();

	public void setId(Long id);

	public String getAccount();

	public void setAccount(String account);

	public String getFromChannel();

	public void setFromChannel(String fromChannel);
	
	public String getAdvertChannel();
	public void setAdvertChannel(String advertChannel);

	public String getRequestIden();

	public void setRequestIden(String requestIden);

	public Date getRequestTime();

	public void setRequestTime(Date requestTime);

	public Date getResponseTime();

	public void setResponseTime(Date responseTime);

	public String getResponseCode();

	public void setResponseCode(String responseCode);

	public String getResponseMessage();

	public void setResponseMessage(String responseMessage);

	public String getClientIp();

	public void setClientIp(String clientIp);

	public String getLocalIp();

	public void setLocalIp(String localIp);

	public String getRequestParams();

	public void setRequestParams(String requestParams);

	public String getResponseData();

	public void setResponseData(String responseData);

	public String getTokenId();
	public void setTokenId(String tokenId);
	
	public String getUserAgent();
	public void setUserAgent(String userAgent);
	
	public String getFromapp();
	public void setFromapp(String fromapp);
	
	public String getTub1();
	public void setTub1(String tub1);
	
	public String getTub2();
	public void setTub2(String tub2);
	
	public String getTub3();
	public void setTub3(String tub3);
}