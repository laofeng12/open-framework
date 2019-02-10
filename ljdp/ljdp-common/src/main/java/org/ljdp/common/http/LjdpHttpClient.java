package org.ljdp.common.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ljdp.component.workflow.Releaseable;

public class LjdpHttpClient extends BaseHttpClient implements Releaseable {
	
	public LjdpHttpClient() {
		this.httpclient = createClientDefault(null);
		this.attributes = new HashMap<String, Object>();
	}
	
	public LjdpHttpClient(String baseURL) {
		this(baseURL, null);
	}

	public LjdpHttpClient(String baseURL, Map<String, Object> settings) {
		this.httpclient = createClientDefault(settings);
		this.attributes = new HashMap<String, Object>();
		this.baseURL = baseURL;
	}
	public LjdpHttpClient(Map<String, Object> settings) {
		this.httpclient = createClientDefault(settings);
		this.attributes = new HashMap<String, Object>();
	}
	
	protected CloseableHttpClient createClientDefault(Map<String, Object> settings) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		int socketTimeout = 30 * 1000;
		int connectTimeout = 3 * 1000;
		if(settings != null) {
			if(settings.containsKey("maxConnTotal")) {
				Integer maxConnTotal = (Integer)settings.get("maxConnTotal");
				httpClientBuilder.setMaxConnTotal(maxConnTotal);
			}
			if(settings.containsKey("maxConnPerRoute")) {
				Integer maxConnPerRoute = (Integer)settings.get("maxConnPerRoute");
				httpClientBuilder.setMaxConnPerRoute(maxConnPerRoute);
			}
			if(settings.containsKey("socketTimeout")) {
				socketTimeout = (Integer)settings.get("socketTimeout");
			}
			if(settings.containsKey("connectTimeout")) {
				connectTimeout = (Integer)settings.get("connectTimeout");
			}
		}
		this.requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true).setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
		return httpClientBuilder.build();
	}
}
