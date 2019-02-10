package org.ljdp.common.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;

public class BaseHttpClient {
	protected CloseableHttpClient httpclient;
	protected Map<String, Object> attributes;
	protected boolean login = false;
	protected String baseURL;
	protected String charEncode = "UTF-8";
	protected RequestConfig requestConfig;
	
	protected List<Header> addHeaders = new ArrayList<>();
	protected List<Header> setHeaders = new ArrayList<>();
	
	public HttpGet createGetRequest(final String url) {
		HttpGet httpGet = new HttpGet(mergeURL(url));
		httpGet.setConfig(requestConfig);
		for (Header h : setHeaders) {
			httpGet.setHeader(h);
		}
		for (Header h : addHeaders) {
			httpGet.addHeader(h);
		}
		return httpGet;
	}
	
	public HttpResponse execute(final HttpUriRequest request) throws IOException, ClientProtocolException {
		return httpclient.execute(request);
	}
	
	public HttpResponse get(final String url) throws IOException, ClientProtocolException {
		HttpGet httpGet = createGetRequest(url);
		return httpclient.execute(httpGet);
	}

	private void initPostHeaders(HttpPost httpPost) {
		for (Header h : setHeaders) {
			httpPost.setHeader(h);
		}
		for (Header h : addHeaders) {
			httpPost.addHeader(h);
		}
	}
	
	private void initPutHeaders(HttpPut httpPut) {
		for (Header h : setHeaders) {
			httpPut.setHeader(h);
		}
		for (Header h : addHeaders) {
			httpPut.addHeader(h);
		}
	}
	
	public HttpPost createPostRequest(final String url, final List<NameValuePair> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(mergeURL(url));
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(new UrlEncodedFormEntity(params, charEncode));
		initPostHeaders(httpPost);
		return httpPost;
	}

	public HttpPost createPostRequest(final String url, final HttpEntity entity) {
		HttpPost httpPost = new HttpPost(mergeURL(url));
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(entity);
		initPostHeaders(httpPost);
		return httpPost;
	}
	
	//===============POST=======================
	public HttpResponse post(final String url, final List<NameValuePair> params) 
			throws ClientProtocolException, IOException {
		HttpPost httpPost = createPostRequest(url, params);
		HttpResponse response = httpclient.execute(httpPost);
		return response;
	}
	
	public HttpResponse post(final String url, final HttpEntity entity) 
			throws ClientProtocolException, IOException {
		HttpPost httpPost = createPostRequest(url, entity);
		HttpResponse response = httpclient.execute(httpPost);
		return response;
	}
	
	public HttpResponse postJSON(String url, String content) throws ClientProtocolException, IOException  {
		HttpPost httpPost = new HttpPost(mergeURL(url));
		httpPost.addHeader("Content-Type", "application/json;charset="+charEncode);
		return doPostContent(httpPost, content);
	}
	public HttpResponse postContent(String url, String content) throws ClientProtocolException, IOException  {
		HttpPost httpPost = new HttpPost(mergeURL(url));
		return doPostContent(httpPost, content);
	}
	public HttpResponse postContent(String url, String contentType, String content) throws ClientProtocolException, IOException  {
		HttpPost httpPost = new HttpPost(mergeURL(url));
		httpPost.addHeader("Content-Type", contentType+";charset="+charEncode);
		return doPostContent(httpPost, content);
	}
	
	//===============PUT=================
	public HttpResponse putJSON(String url, String content) throws ClientProtocolException, IOException  {
		HttpPut httpPut = new HttpPut(mergeURL(url));
		httpPut.addHeader("Content-Type", "application/json;charset="+charEncode);
		return doPutContent(httpPut, content);
	}
	public HttpResponse putContent(String url, String content) throws ClientProtocolException, IOException  {
		HttpPut httpPut = new HttpPut(mergeURL(url));
		return doPutContent(httpPut, content);
	}
	public HttpResponse putContent(String url, String contentType, String content) throws ClientProtocolException, IOException  {
		HttpPut httpPut = new HttpPut(mergeURL(url));
		httpPut.addHeader("Content-Type", contentType+";charset="+charEncode);
		return doPutContent(httpPut, content);
	}
	
	protected HttpResponse doPostContent(HttpPost httpPost, String content) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		httpPost.setConfig(requestConfig);

		byte[] b = content.getBytes(charEncode);
		InputStream byteInputStream = new ByteArrayInputStream(b, 0, b.length);
		HttpEntity requestEntity = new InputStreamEntity(byteInputStream,
				b.length);
		httpPost.setEntity(requestEntity);

		initPostHeaders(httpPost);
		
		HttpResponse response = httpclient.execute(httpPost);
		return response;
	}
	
	protected HttpResponse doPutContent(HttpPut httpPut, String content) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		httpPut.setConfig(requestConfig);

		byte[] b = content.getBytes(charEncode);
		InputStream byteInputStream = new ByteArrayInputStream(b, 0, b.length);
		HttpEntity requestEntity = new InputStreamEntity(byteInputStream,
				b.length);
		httpPut.setEntity(requestEntity);

		initPutHeaders(httpPut);
		
		HttpResponse response = httpclient.execute(httpPut);
		return response;
	}
	
	private String mergeURL(String url) {
		if(baseURL == null) {
			return url;
		}
		if(url.startsWith("http:") || url.startsWith("HTTP:")) {
			return url;
		}
		if(baseURL.endsWith("/") && url.startsWith("/")) {
			url = url.substring(1, url.length());
		}
		return baseURL + url;
	}
	
	public void release() {
		if (httpclient != null) {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public CloseableHttpClient getHttpClient() {
		return httpclient;
	}
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getCharEncode() {
		return charEncode;
	}

	public void setCharEncode(String charEncode) {
		this.charEncode = charEncode;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public void addHeader(String name, String value) {
		addHeaders.add(new BasicHeader(name, value));
	}
	
	public void setHeader(String name, String value) {
		setHeaders.add(new BasicHeader(name, value));
	}
	
}
