package org.ljdp.common.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONTools {
	private static Logger log = LoggerFactory.getLogger(JSONTools.class);
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String GMT_TIME = "time";
	
	/**
	 * 配置json-lib需要的excludes和datePattern.
	 * 
	 * @param excludes
	 *            不需要转换的属性数组
	 * @param datePattern
	 *            日期转换模式
	 * @return JsonConfig 根据excludes和dataPattern生成的jsonConfig，用于write
	 */
	public static JsonConfig config(String[] excludes, String datePattern) {
		if(StringUtils.isEmpty(datePattern)) {
			datePattern = DATE_PATTERN;
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		if(datePattern.equalsIgnoreCase(GMT_TIME)) {
			jsonConfig.registerJsonValueProcessor(Date.class,
					new TimeValueProcessor());
			jsonConfig.registerJsonValueProcessor(java.sql.Date.class,
					new TimeValueProcessor());
			jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,
					new TimeValueProcessor());
		} else {
			jsonConfig.registerJsonValueProcessor(Date.class,
					new DateValueProcessor(datePattern));
			jsonConfig.registerJsonValueProcessor(java.sql.Date.class,
					new DateValueProcessor(datePattern));
			jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,
					new DateValueProcessor(datePattern));
		}
		jsonConfig.registerJsonValueProcessor(Float.class, new DecimalValueProcessor());
		jsonConfig.registerJsonValueProcessor(Double.class, new DecimalValueProcessor());
		return jsonConfig;
	}
	
	public static JsonConfig config() {
		return config(null, null);
	}
	
	public static String toJSON(Object bean, String datePattern) {
		JsonConfig jsonConfig = config(null, datePattern);
		JSON json = JSONSerializer.toJSON(bean, jsonConfig);
		return json.toString();
	}
	
	public static String toJSON(Object bean) {
		return toJSON(bean, DATE_PATTERN);
	}
	
	public static void writePage(Object bean, String[] excludes, HttpServletResponse response,
			String datePattern, String contentType) {
		JsonConfig jsonConfig = config(excludes, datePattern);
		JSON json = JSONSerializer.toJSON(bean, jsonConfig);
		if(log.isDebugEnabled()) {
			log.debug("write page: " + json.toString());
		}
		response.setContentType(contentType);
		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
	
	public static void writePage(Object bean, HttpServletResponse response,
			String datePattern, String contentType) {
		writePage(bean, null, response, datePattern, contentType);
	}
	
	public static void writePage(Object bean, HttpServletResponse response,
			String datePattern) {
		writePage(bean, response, datePattern, "text/json;charset=utf-8");
	}
	
	public static void writePage(Object bean, String[] excludes, HttpServletResponse response,
			String datePattern) {
		writePage(bean, excludes, response, datePattern, "text/json;charset=utf-8");
	}
	
	public static void writePage(Object bean, HttpServletResponse response) {
		writePage(bean, response, DATE_PATTERN);
	}
	
	public static void writePage(Object bean, String[] excludes, HttpServletResponse response) {
		writePage(bean, excludes, response, DATE_PATTERN);
	}
	
	public static void writeHTML(Object bean, HttpServletResponse response) {
		writePage(bean, response, DATE_PATTERN, "text/html;charset=utf-8");
	}
	
	public static Object toJavaObject(String jsonStr) {
		JsonConfig jsonConfig = config(null, "yyyy-MM-dd");
		JSON json = JSONObject.fromObject(jsonStr, jsonConfig);
		Object jo = JSONSerializer.toJava(json);
		return jo;
	}
	
	@SuppressWarnings("rawtypes")
	public static Collection toJavaCollection(String jsonStr) {
		JsonConfig jsonConfig = config(null, "yyyy-MM-dd");
		JSONArray jsonArray = JSONArray.fromObject(jsonStr, jsonConfig);
		return JSONArray.toCollection(jsonArray);
	}
	
	@SuppressWarnings("rawtypes")
	public static Object toJavaBean(String jsonStr, Class beanClass) {
		JsonConfig jsonConfig = config(null, "yyyy-MM-dd");
		JSONObject json = JSONObject.fromObject(jsonStr, jsonConfig);
		Object obj = JSONObject.toBean(json, beanClass);
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public static Collection toJavaObjectCollection(String jsonStr, Class clazz) {
		JsonConfig jsonConfig = config(null, "yyyy-MM-dd");
		JSONArray arr = JSONArray.fromObject(jsonStr, jsonConfig);
		return JSONArray.toCollection(arr, clazz);
	}
}
