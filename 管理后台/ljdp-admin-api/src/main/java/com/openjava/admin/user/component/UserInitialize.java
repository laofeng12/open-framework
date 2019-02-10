package com.openjava.admin.user.component;

import java.util.Map;

import org.ljdp.component.user.BaseUserInfo;

/**
 * 用户初始化通用组件
 * @author hzy0769
 *
 */
public interface UserInitialize {

	public BaseUserInfo onLogin(BaseUserInfo user, Map<String, String> extDatas);
}
