package org.ljdp.api.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.ljdp.api.expection.UnKnowContentException;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;
import org.ljdp.common.config.Env;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.json.JacksonTools;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiClient {

	private static LjdpHttpClient client;
	
	public static final String PARAM_TOKEN = "tokenid";
	
	public static String CLIENT_CHANNEL = "";
	
	static {
		ConfigFile cfg = Env.current().getConfigFile();
		String url = cfg.getValue("api.url");
		Map<String, Object> httpSettings = new HashMap<>();
		String maxConnPerRoute = cfg.getValue("api.maxConnPerRoute");
		if(StringUtils.isNotBlank(maxConnPerRoute)) {
			httpSettings.put("maxConnPerRoute", new Integer(maxConnPerRoute));
		}
		String maxConnTotal = cfg.getValue("api.maxConnTotal");
		if(StringUtils.isNotBlank(maxConnTotal)) {
			httpSettings.put("maxConnTotal", new Integer(maxConnTotal));
		}
		
		
		ConfigFile sysCf = ConfigFileFactory.getInstance().get("sys");
		if(sysCf != null) {
			CLIENT_CHANNEL = sysCf.getValue("sys_module_id");
			if(CLIENT_CHANNEL == null) {
				CLIENT_CHANNEL = "";
			}
		}
		
		System.out.println("api.url="+url);
		System.out.println("api.maxConnPerRoute="+maxConnPerRoute);
		System.out.println("api.maxConnTotal="+maxConnTotal);
		System.out.println("client.channel="+CLIENT_CHANNEL);
		client = new LjdpHttpClient(url, httpSettings);
	}
	
	public static LjdpHttpClient getHttpClient() {
		return client;
	}

	private static <T> T readResponse(final HttpResponse apiResp, final Class<T> cls)
			throws IOException, JsonParseException, JsonMappingException, UnKnowContentException {
		HttpEntity entity = apiResp.getEntity();
		if(entity == null) {
			return null;
		}
		if(entity.getContentType() == null) {
			return null;
		}
		String ct = entity.getContentType().getValue();
		if(ct.startsWith("text/json") || ct.startsWith("application/json")) {
			ObjectMapper omapper = JacksonTools.getObjectMapper();
			return omapper.readValue(entity.getContent(), cls);
		}
		throw new UnKnowContentException(HttpClientUtils.getContentString(apiResp.getEntity(), "utf-8"));
	}
	private static Object readResponse(final HttpResponse apiResp, final JavaType javaType)
			throws IOException, JsonParseException, JsonMappingException, UnKnowContentException {
		HttpEntity entity = apiResp.getEntity();
		if(entity == null) {
			return null;
		}
		if(entity.getContentType() == null) {
			return null;
		}
		String ct = entity.getContentType().getValue();
		if(ct.startsWith("text/json") || ct.startsWith("application/json")) {
			ObjectMapper omapper = JacksonTools.getObjectMapper();
			return omapper.readValue(entity.getContent(), javaType);
		}
		throw new UnKnowContentException(HttpClientUtils.getContentString(apiResp.getEntity(), "utf-8"));
	}
	private static <T> T readResponse(final HttpResponse apiResp, final TypeReference<T> typeref)
			throws IOException, JsonParseException, JsonMappingException, UnKnowContentException {
		String ct = apiResp.getEntity().getContentType().getValue();
		if(ct.startsWith("text/json") || ct.startsWith("application/json")) {
			ObjectMapper omapper = JacksonTools.getObjectMapper();
			return omapper.readValue(apiResp.getEntity().getContent(), typeref);
		}
		throw new UnKnowContentException(HttpClientUtils.getContentString(apiResp.getEntity(), "utf-8"));
	}
	
	public static <T> T doGet(ApiRequestEntity req, final Class<T> cls) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doGet(req);
		return readResponse(apiResp, cls);
	}
	
	public static <T> T doGet(ApiRequestEntity req, final Class<?> parametrized, final Class<T> parametersFor, final Class<?> parameterClasses) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doGet(req);
		JavaType type = JacksonTools.constructParametrizedType(parametrized, parametersFor, parameterClasses);
		return (T)readResponse(apiResp, type);
	}
	
	public static Object doGet(ApiRequestEntity req, final JavaType javaType) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doGet(req);
		return readResponse(apiResp, javaType);
	}
	
	public static <T> T doGet(ApiRequestEntity req, final TypeReference<T> typeRef) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doGet(req);
		return readResponse(apiResp, typeRef);
	}

	public static HttpResponse doGet(ApiRequestEntity req) throws IOException, ClientProtocolException {
		if(req.getRequest() != null) {
			setToken(req);
			setChannel(req);
			setUserAgent(req, true);
			setTub(req);
		}
		req.joinParamsToURL();
		HttpGet get = client.createGetRequest(req.getUrl());
		if(req.getRequest() != null) {
			get.addHeader("x-forwarded-for", getClientIP(req.getRequest()));
			get.setHeader("user-agent", getUserAgent(req.getRequest(), false));
		}
		HttpResponse apiResp = client.execute(get);
		return apiResp;
	}

	public static <T> T doPost(ApiRequestEntity req, final Class<T> cls) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doPost(req);
		return readResponse(apiResp, cls);
	}
	
	public static <T> T doPost(ApiRequestEntity req, final Class<?> parametrized, final Class<T> parametersFor, final Class<?> parameterClasses) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doPost(req);
		JavaType type = JacksonTools.constructParametrizedType(parametrized, parametersFor, parameterClasses);
		return (T)readResponse(apiResp, type);
	}
	
	public static Object doPost(ApiRequestEntity req, final JavaType javaType) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doPost(req);
		return readResponse(apiResp, javaType);
	}
	
	public static <T> T doPost(ApiRequestEntity req, final TypeReference<T> typeRef) throws UnKnowContentException, ClientProtocolException, IOException {
		HttpResponse apiResp = doPost(req);
		return readResponse(apiResp, typeRef);
	}

	public static HttpResponse doPost(ApiRequestEntity req)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		if(req.getRequest() != null) {
			setToken(req);
			setChannel(req);
			setUserAgent(req, false);
			setTub(req);
		}
		HttpPost post = client.createPostRequest(req.getUrl(), req.getParams());
		if(req.getRequest() != null) {
			post.addHeader("x-forwarded-for", getClientIP(req.getRequest()));
		}
		HttpResponse apiResp = client.execute(post);
		return apiResp;
	}
	
	/**
	 * application/json方式提交
	 * @param req
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse doPostJson(ApiRequestEntity req) throws ClientProtocolException, IOException {
		byte[] b = req.getBodyJsonContent();
		InputStream byteInputStream = new ByteArrayInputStream(b, 0, b.length);
		HttpEntity requestEntity = new InputStreamEntity(byteInputStream,
				b.length);
		HttpPost post = client.createPostRequest(req.getUrl(), requestEntity);
		post.addHeader("Content-Type", "application/json;charset=utf-8");
		if(req.getRequest() != null) {
			post.addHeader("x-forwarded-for", getClientIP(req.getRequest()));
			String tokenid;
			if(StringUtils.isNotBlank(req.getToken())) {
				tokenid = req.getToken();
			} else {
				tokenid = getToken(req.getRequest());
			}
			post.addHeader("authority-token", tokenid);
			if(StringUtils.isNotBlank(req.getUserAgent())) {
				post.addHeader("user-agent", req.getUserAgent());
			} else {
				post.addHeader("user-agent", getUserAgent(req.getRequest(), false));
			}
		} else {
			post.addHeader("authority-token", req.getToken());
			post.addHeader("user-agent", req.getUserAgent());
		}
		HttpResponse apiResp = client.execute(post);
		return apiResp;
	}
	
	public static <T> T doPostJson(ApiRequestEntity req, final Class<T> cls) throws ClientProtocolException, IOException, UnKnowContentException {
		HttpResponse apiResp = doPostJson(req);
		return readResponse(apiResp, cls);
	}
	
	public static <T> T doPostJson(ApiRequestEntity req, final Class<?> parametrized, final Class<T> parametersFor, final Class<?> parameterClasses) throws ClientProtocolException, IOException, UnKnowContentException {
		HttpResponse apiResp = doPostJson(req);
		JavaType type = JacksonTools.constructParametrizedType(parametrized, parametersFor, parameterClasses);
		return (T)readResponse(apiResp, type);
	}
	
	public static Object doPostJson(ApiRequestEntity req, final JavaType javaType) throws ClientProtocolException, IOException, UnKnowContentException {
		HttpResponse apiResp = doPostJson(req);
		return readResponse(apiResp, javaType);
	}
	
	public static <T> T doPostJson(ApiRequestEntity req, final TypeReference<T> typeRef) throws ClientProtocolException, IOException, UnKnowContentException {
		HttpResponse apiResp = doPostJson(req);
		return readResponse(apiResp, typeRef);
	}
	
	public static boolean setToken(final ApiRequestEntity req) {
//		if(req.containParam(PARAM_TOKEN)) {
//			return true;
//		}
		HttpServletRequest request = req.getRequest();
		String tokenid = getToken(request);
		if(tokenid != null) {
			req.addParam(PARAM_TOKEN, tokenid);
			return true;
		}
		return false;
	}
	
	public static void setChannel(final ApiRequestEntity req) {
		if(CLIENT_CHANNEL.equals("100015")) {//APP,M-API进来
			String client_id = (String)req.getParam("client_id");
			if(StringUtils.isNotBlank(client_id)) {
				//有做安全认证
				if(!client_id.equals("100015")) {
					//第三方平台访问，直接记录第三方平台的id
					req.setChannel(client_id);
				} else {
					//自己的app访问，尝试解析区分手机类型
					setAppDeviceType(req);
				}
				
				//认证授权信息不用给后端，由前端网关处理
				req.removeParam("client_id");
				req.removeParam("client_pwd");
				req.removeParam("sign");
			}
		} else if(CLIENT_CHANNEL.equals("100007")
				|| CLIENT_CHANNEL.equals("100024")) {//wap端进来
			//通过浏览器访问，尝试解析区分手机类型
			setBrowserDeviceType(req);
		}
		
		if(req.getChannel() != null) {
			req.addParam("fromChannel", req.getChannel());
		} else {
			req.addParam("fromChannel", CLIENT_CHANNEL);
		}
		HttpServletRequest request = req.getRequest();
		String advertChannel = getAdvertChannel(request);
		if(advertChannel != null) {
			req.addParam("fromChannel2", advertChannel);
		}
	}

	public static void setBrowserDeviceType(final ApiRequestEntity req) {
		try {
			String uagent = getUserAgent(req.getRequest(), false);
			String tmpagent = uagent.toLowerCase();
			if(tmpagent.indexOf("micromessenger") >= 0) {
				req.setChannel("100008");//微信内置浏览器
			} else if(tmpagent.indexOf("android") >= 0) {
				req.setChannel("100021");
			} else if(tmpagent.indexOf("ios") >= 0 || tmpagent.indexOf("iphone") >= 0
					|| tmpagent.indexOf("ipad") >= 0) {
				req.setChannel("100022");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setAppDeviceType(final ApiRequestEntity req) {
		try {
			String uagent = getUserAgent(req.getRequest(), false);
			String tmpagent = uagent.toLowerCase();
			if(tmpagent.indexOf("android") >= 0) {
				req.setChannel("100005");
			} else if(tmpagent.indexOf("iphone") >= 0 || tmpagent.indexOf("ios") >= 0) {
				req.setChannel("100006");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getAdvertChannel(HttpServletRequest request) {
		HttpSession httpsession = request.getSession(false);
		String advertChannel = null;
		if(httpsession != null) {
			advertChannel = (String)httpsession.getAttribute("fromadvert");
		}
		return advertChannel;
	}
	
	public static void setUserAgent(final ApiRequestEntity req, boolean isGet) {
		try {
			HttpServletRequest request = req.getRequest();
			String uagent = getUserAgent(request, isGet);
			req.addParam("userAgent", uagent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUserAgent(HttpServletRequest request, boolean isGet) throws UnsupportedEncodingException {
		String uagent = request.getHeader("user-agent");
		if(isGet) {
			uagent = URLEncoder.encode(uagent, "iso-8859-1");
		}
		return uagent;
	}
	
	public static String getToken(final HttpServletRequest request) {
		HttpSession httpsession = request.getSession(false);
		if(httpsession != null) {
			String tokenid = (String)httpsession.getAttribute("security.auth.token");
			if(tokenid != null) {
				return tokenid;
			}
		}
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals(PARAM_TOKEN)) {
					if(StringUtils.isNotEmpty(cookies[i].getValue())) {
						return cookies[i].getValue();
					}
				}
			}
		}
		return null;
	}
	
	public static void setTub(final ApiRequestEntity req) {
		if(req.containParam("tub")) {
			return;
		}
		HttpServletRequest request = req.getRequest();
		String tub = request.getParameter("tub");
		if(StringUtils.isNotBlank(tub)) {
			req.addParam("tub", tub);
		}
	}
	
	public static String getClientIP(final HttpServletRequest req) {
		String clientIP = null;
		String realIp = req.getHeader("X-Real-IP");
		if(StringUtils.isNotBlank(realIp)) {
			clientIP = realIp;
		}
		if(clientIP == null) {
			realIp = req.getHeader("x-forwarded-for");
			if(StringUtils.isNotBlank(realIp)) {
				clientIP = realIp;
			}
		}
		if(clientIP == null) {
			clientIP = req.getRemoteAddr();
		}
		
		return clientIP;
	}
}
