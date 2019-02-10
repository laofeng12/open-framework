package org.openjava.boot.conf;

import java.util.Map;

import org.ljdp.component.user.BaseUserInfo;
import org.springframework.stereotype.Component;

import com.openjava.admin.user.component.UserInitialize;

@Component("userInitialize")
public class ExampleUserInitialize implements UserInitialize {

	@Override
	public BaseUserInfo onLogin(BaseUserInfo user, Map<String, String> extDatas) {
		System.out.println("【ExampleUserInitialize】");
		System.out.println(user);
		return null;
	}

}
