package org.ljdp.common.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	public static String getLocation(HttpResponse response) {
		String url = null;
		Header[] headers = response.getHeaders("Location");
		if (headers.length > 0) {
			url = headers[0].getValue();
		}
		return url;
	}

	public static HttpResponse httpGetURL(HttpClient httpclient, String url)
			throws IOException, ClientProtocolException {
		RequestConfig requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(true).setSocketTimeout(60 * 1000)
				.setConnectTimeout(60 * 1000).build();

		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		return httpclient.execute(httpGet);
	}

	public static HttpResponse postContent(CloseableHttpClient client,
			String url, String content, String charEncode) throws ClientProtocolException, IOException  {
		HttpPost httpPost = new HttpPost(url);

		return doPostContent(client, httpPost, content, charEncode);
	}
	
	public static HttpResponse postJSON(CloseableHttpClient client,
			String url, String content, String charEncode) throws ClientProtocolException, IOException  {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		return doPostContent(client, httpPost, content, charEncode);
	}

	protected static HttpResponse doPostContent(CloseableHttpClient client, HttpPost httpPost, String content,
			String charEncode) throws UnsupportedEncodingException, IOException, ClientProtocolException {
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(60 * 1000).setConnectTimeout(60 * 1000)
				.build();
		httpPost.setConfig(requestConfig);

		byte[] b = content.getBytes(charEncode);
		InputStream byteInputStream = new ByteArrayInputStream(b, 0, b.length);
		HttpEntity requestEntity = new InputStreamEntity(byteInputStream,
				b.length);
		httpPost.setEntity(requestEntity);

		HttpResponse response = client.execute(httpPost);
		return response;
	}

	public static HttpResponse postParams(CloseableHttpClient client,
			String url, List<NameValuePair> params, String charEncode) 
			throws ClientProtocolException, IOException {
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(60 * 1000).setConnectTimeout(60 * 1000)
				.build();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);

		httpPost.setEntity(new UrlEncodedFormEntity(params, charEncode));

		HttpResponse response = client.execute(httpPost);
		return response;
	}

	public static void accessHttpUrl(HttpClient httpclient, String url,
			boolean print) throws IOException, ClientProtocolException {
		HttpResponse response = httpGetURL(httpclient, url);
		boolean redirect = false;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
			String redirectURL = getLocation(response);
			if (redirectURL != null) {
				redirect = true;
				EntityUtils.consume(response.getEntity());
				System.out.println("redirect:" + redirectURL);
				accessHttpUrl(httpclient, redirectURL, print);
			}
		}
		if (!redirect) {
			if (print) {
				System.out.println(getHeaderInfo(response));
				System.out
						.println(getContentString(response.getEntity(), "gbk"));
			} else {
				EntityUtils.consume(response.getEntity());
			}
		}
	}

	public static String getHeaderInfo(HttpResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------------\n");
		sb.append(response.getStatusLine()).append("\n");
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			sb.append(h.toString()).append("\n");
		}
		sb.append("---------------------------------------\n");
		return sb.toString();
	}

	public static String getContentString(HttpEntity entity, String charset)
			throws ParseException, IOException {
		if (entity != null) {
			return EntityUtils.toString(entity, charset);
		}
		return "";
	}
	
	public static void copyToServletResponse(HttpServletResponse resp, HttpResponse apiResp) throws IOException {
		Header[] allHeads = apiResp.getAllHeaders();
		for (int i = 0; i < allHeads.length; i++) {
			String headname = allHeads[i].getName();
			if(headname.equals("Connection")
					|| headname.equals("Date")
					|| headname.equals("Server")
					|| headname.equals("Transfer-Encoding")) {
				continue;
			}
			resp.addHeader(headname, allHeads[i].getValue());
		}
//		resp.setContentType("text/json;charset=UTF-8");
		StatusLine sl = apiResp.getStatusLine();
		resp.setStatus(sl.getStatusCode());
		byte[] datas = EntityUtils.toByteArray(apiResp.getEntity());
//		System.out.println("======API2================");
//		System.out.println(new String(datas));
		resp.getOutputStream().write(datas);
//		resp.getOutputStream().flush();
		resp.getOutputStream().close();
	}
}