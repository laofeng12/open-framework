package org.openjava.boot;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.json.JacksonTools;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.openjava.framework.validate.RedisSessionVaidator;

public class ExampleRedisSessionVaidator extends RedisSessionVaidator {
	
	public ExampleRedisSessionVaidator() {
		super();
	}

	public ExampleRedisSessionVaidator(String apiSkey) {
		super(apiSkey);
	}

	@Override
	protected void parseUser(String userJson) throws IOException, JsonParseException, JsonMappingException {
		UserVO user = JacksonTools.getObjectMapper().readValue(userJson, UserVO.class);
		SsoContext.setUser(user);
		if(StringUtils.isNumeric(user.getUserId())) {
			SsoContext.setUserId(new Long(user.getUserId()));
		}
		SsoContext.setAccount(user.getUserAccount());
	}

}
