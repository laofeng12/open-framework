package org.ljdp.secure.permission;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;
import org.ljdp.component.permission.PermissionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过IP来判断访问权限
 * @author hzy
 *
 */
public class IPRestrictChecker implements PermissionChecker {
	
	public Logger log = LoggerFactory.getLogger(this.getClass());
	
	public Set<String> enableSets;

	@Override
	public boolean checkURIPermission(String oprcode, String accessURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkPermission(String oprcode, String resourceId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isProtectedURI(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggerURI(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLogin(HttpServletRequest request) {
		if(enableSets == null) {
			initConfig();
		}
		String clientIp = request.getRemoteAddr();
		if(log.isDebugEnabled()) {
			log.debug(clientIp);
		}
		if(enableSets.contains(clientIp)) {
			return true;
		}
		return false;
	}

	private void initConfig() {
		ConfigFile cf = ConfigFileFactory.getInstance().get("iprestrict");
		String permitIp = cf.getValue("permit_ip");
		if(StringUtils.isNotBlank(permitIp)) {
			enableSets = new HashSet<>();
			String[] items = permitIp.split(",");
			for (int i = 0; i < items.length; i++) {
				enableSets.add(items[i]);
			}
		}
	}
}
