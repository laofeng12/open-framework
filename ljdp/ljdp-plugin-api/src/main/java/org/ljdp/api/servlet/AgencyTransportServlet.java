package org.ljdp.api.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.ljdp.api.client.ApiClient;
import org.ljdp.api.client.ApiRequestEntity;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.plugin.batch.task.FileUploadTask;
import org.ljdp.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转发前端请求至后端API处理，然后把API返回的报文原封不动返回前端
 * @author hzy
 *
 */
public class AgencyTransportServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5737639850603942155L;
	
	private static final String Param_File_Head = "file_";

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String apiURL;//后端API的地址
	private LjdpHttpClient client;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		apiURL = config.getInitParameter("api-url");
		if(StringUtils.isNotBlank(apiURL)) {
			log.info("API-URL="+apiURL);
			client = new LjdpHttpClient(apiURL);
		} else {
			client = ApiClient.getHttpClient();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUrl = getApiRequestRoute(req);
		if(log.isDebugEnabled()) {
			log.debug("GET "+requestUrl);
		}
		//获取api认证参数
		ApiRequestEntity apireq = new ApiRequestEntity(req);
		apireq.addParam(ApiClient.PARAM_TOKEN, ApiClient.getToken(req));
		apireq.addParam("fromChannel", ApiClient.CLIENT_CHANNEL);
		apireq.addParam("fromChannel2", ApiClient.getAdvertChannel(req));
		apireq.addParam("userAgent", ApiClient.getUserAgent(req, true));
		apireq.setUrl(requestUrl);
		apireq.joinParamsToURL();
		requestUrl = apireq.getUrl();//参数加入到get请求
		
//		System.out.println("[AgencyTransportServlet]GET "+requestUrl);
		HttpGet get = client.createGetRequest(requestUrl);
		get.addHeader("x-forwarded-for", ApiClient.getClientIP(req));
		HttpResponse apiResp = client.execute(get);
		writeApiResponse(resp, apiResp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestUrl = getApiRequestRoute(req);
		if(log.isDebugEnabled()) {
			log.debug("POST "+requestUrl+" "+req.getHeader("Content-Type"));
		}
		String clientIP = ApiClient.getClientIP(req);
		
		boolean isMultipart= ServletFileUpload.isMultipartContent(req);
		Map<String, List<FileUploadTask>> fileTaskMap;
		try {
			fileTaskMap = getUploadMemoryFileList(req);
			if(fileTaskMap.size() > 0) {
				isMultipart = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse apir = new BasicApiResponse(APIConstants.CODE_FAILD, e.getMessage());
			JacksonTools.writePage(apir, resp);
			return;
		}
		//获取api认证参数
		ApiRequestEntity apireq = new ApiRequestEntity(req);
		apireq.addParam(ApiClient.PARAM_TOKEN, ApiClient.getToken(req));
		apireq.addParam("fromChannel", ApiClient.CLIENT_CHANNEL);
		apireq.addParam("fromChannel2", ApiClient.getAdvertChannel(req));
		apireq.addParam("userAgent", ApiClient.getUserAgent(req, false));
		List<NameValuePair> authParams = apireq.getParams();
		
//		if(log.isDebugEnabled()) {
//			System.out.println("=========================================================");
//			System.out.println(req.getRequestURI());
//			System.out.println("session="+req.getSession().getId());
//			System.out.println("token="+ApiClient.getToken(req));
//			System.out.println("userAgent="+ApiClient.getUserAgent(req, false));
//			System.out.println("isMultipart="+isMultipart);
//			System.out.println("=========================================================");
//		}
		
		if(!isMultipart) {
			List<NameValuePair> params = new ArrayList<>();
			Enumeration<String> en = req.getParameterNames();
			while (en.hasMoreElements()) {
				String name = (String) en.nextElement();
				String value = req.getParameter(name);
				if(log.isDebugEnabled()) {
					log.debug(name+"="+value);
				}
				params.add(new BasicNameValuePair(name, value));
			}
			for (NameValuePair ap : authParams) {
				params.add(ap);
			}
			
			HttpPost post = client.createPostRequest(requestUrl, params);
			post.addHeader("x-forwarded-for", clientIP);
			HttpResponse apiResp = client.execute(post);
			writeApiResponse(resp, apiResp);
		} else {
			//构造一个文件上传处理对象
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*20);//
			ServletFileUpload upload = new ServletFileUpload(factory);
			//解析表单中提交的所有文件内容
			try {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.RFC6532);
				builder.setCharset(Charset.forName(req.getCharacterEncoding()));
				List<?> list = upload.parseRequest(req);
				for(int i = 0; i < list.size(); i++) {
					FileItem item = (FileItem)list.get(i);
					if(log.isDebugEnabled()) {
						log.debug(item.getFieldName()+", "+item.getName());
					}
					if(item.getSize() > 0) {
						if (item.isFormField()) {
							String name = item.getFieldName();
							if(name.startsWith(Param_File_Head)) {
								continue;
							}
							String value;
							if(req.getCharacterEncoding() != null) {
								value = item.getString(req.getCharacterEncoding());
							} else {
								value = item.getString();
							}
							builder.addTextBody(name, value, ContentType.create("text/plain", req.getCharacterEncoding()));
						} else {
							builder.addBinaryBody(item.getFieldName(), item.get(), ContentType.create(item.getContentType(), req.getCharacterEncoding()), item.getName());
						}
					}
				}
				
				for (NameValuePair ap : authParams) {
					builder.addTextBody(ap.getName(), ap.getValue(), ContentType.create("text/plain", req.getCharacterEncoding()));
				}
				
				if(fileTaskMap.size() > 0) {
					Iterator<String> it = fileTaskMap.keySet().iterator();
					while (it.hasNext()) {
						String pname = (String) it.next();
						List<FileUploadTask> flist = fileTaskMap.get(pname);
						for (FileUploadTask ftask : flist) {
							byte[] fcontents = FileUtils.readFileToByteArray(new File(ftask.getSaveFileName()));
							builder.addBinaryBody(pname, fcontents, ContentType.create(ftask.getUploadContentType(), req.getCharacterEncoding()), ftask.getUploadFileName());
						}
					}
				}
				HttpPost post = client.createPostRequest(requestUrl, builder.build());
				post.addHeader("x-forwarded-for", clientIP);
				HttpResponse apiResp = client.execute(post);
				writeApiResponse(resp, apiResp);
			} catch (FileUploadException e) {
				e.printStackTrace();
				throw new ServletException(e);
			}
		}
	}

	private Map<String, List<FileUploadTask>> getUploadMemoryFileList(HttpServletRequest req) throws Exception {
		Map<String, List<FileUploadTask>> map = new HashMap<>();
		Enumeration<String> pen = req.getParameterNames();
		while (pen.hasMoreElements()) {
			String name = (String) pen.nextElement();
			if(name.startsWith(Param_File_Head)) {
				List<FileUploadTask> fileList = new ArrayList<>();
				String value = req.getParameter(name);
				String[] items = value.split(",");
				for (String fid : items) {
					FileUploadTask task = (FileUploadTask)MemoryCache.getData("ljdp-attach-id", fid);
					if(task != null) {
						fileList.add(task);
					} else {
						throw new Exception("文件失效，请重新上传");
					}
				}
				map.put(name, fileList);
			}
		}
		return map;
	}

	/**
	 * 获取请求后端API的路径
	 * @param req
	 * @return
	 */
	private String getApiRequestRoute(HttpServletRequest req) {
		String path = req.getContextPath();
		String basePath;
		if(req.getServerPort() == 80) {
			basePath = req.getScheme()+"://"+req.getServerName()+path;
		} else {
			basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;
		}
		
//		String tokenid = ApiClient.getToken(req);
		
		String requestUrl = req.getRequestURL().toString();
		requestUrl = requestUrl.replaceFirst(basePath, "");
		String queryString = req.getQueryString();
		if (null != queryString) {
			requestUrl += "?" + queryString;
			/*if(tokenid != null) {
				requestUrl += "&"+ApiClient.PARAM_TOKEN+"="+tokenid;
			}*/
		} else {
			/*if(tokenid != null) {
				requestUrl += "?"+ApiClient.PARAM_TOKEN+"="+tokenid;
			}*/
		}
		return requestUrl;
	}

	/**
	 * 将API返回的结果原封不动返回
	 * @param resp
	 * @param apiResp
	 * @throws IOException
	 */
	private void writeApiResponse(HttpServletResponse resp, HttpResponse apiResp) throws IOException {
		HttpClientUtils.copyToServletResponse(resp, apiResp);
	}

	@Override
	public void destroy() {
		super.destroy();
		try {
			client.getHttpClient().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
