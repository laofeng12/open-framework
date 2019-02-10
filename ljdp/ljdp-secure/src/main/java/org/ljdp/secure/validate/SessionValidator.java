package org.ljdp.secure.validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.secure.annotation.Security;

/**
 * 登陆session验证器
 * @author hzy
 *
 */
public interface SessionValidator {
	public ApiResponse validate(HttpServletRequest request, HttpServletResponse response, Security secure);
}
