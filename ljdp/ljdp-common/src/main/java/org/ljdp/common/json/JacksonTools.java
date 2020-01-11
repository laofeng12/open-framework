package org.ljdp.common.json;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTools {
	private static final ObjectMapper om = new ObjectMapper();
	
	static {
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		om.setSerializationInclusion(Include.NON_NULL);
	}
	
	public static final void writePage(final Object bean, final HttpServletResponse response, final String contentType) throws JsonGenerationException, JsonMappingException, IOException {
		response.setContentType(contentType);
		om.writeValue(response.getOutputStream(), bean);
	}
	
	public static final void writePage(final Object bean, final HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		writePage(bean, response, "text/json;charset=utf-8");
	}
	
	public static ObjectMapper getObjectMapper() {
		return om;
	}
	
	public static JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, Class<?>... parameterClasses) {
		return om.getTypeFactory().constructParametrizedType(parametrized,parametersFor,parameterClasses);
	}
	
	public static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
		return om.getTypeFactory().constructParametricType(parametrized,parameterClasses);
	}
}
