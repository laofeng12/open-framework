package com.openjava.framework.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.cache.CacheUtil;
import org.ljdp.cache.ICacheManager;
import org.ljdp.component.permission.PermissionChecker;
import org.ljdp.component.session.Session;

import net.rubyeye.xmemcached.MemcachedClient;

public class LmAdminLoginChecker implements PermissionChecker {

	@Override
	public boolean checkPermission(String arg0, String arg1) {
		return false;
	}

	@Override
	public boolean checkURIPermission(String arg0, String arg1) {
		return false;
	}

	@Override
	public boolean isLoggerURI(String arg0) {
		return false;
	}

	@Override
	public boolean isLogin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			Object t = session.getAttribute("security.auth.token");
			if(t != null) {
				return true;
			}
		}
		String tokenid = getValueByName("tokenid", request);
		if(StringUtils.isNotBlank(tokenid)) {
			ICacheManager<MemcachedClient> manager = CacheUtil.getCacheManager();
			MemcachedClient mclient = manager.getCache("xSessionCache");
			String xsessionkey = "xs_"+tokenid;
			try {
				Session xsession = (Session)mclient.get(xsessionkey);
				Object t = xsession.getAttribute("security.auth.token");
				if(t != null) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static String getValueByName(String name,HttpServletRequest request)
    {
    	if(request==null)return "";
        Cookie cookies[]=request.getCookies();
        Cookie sCookie=null;
        String svalue=null;
        String sname=null;

        if(cookies==null)
            return null;
        for(int i=0;i< cookies.length;i++)
        {
            sCookie=cookies[i];
            sname=sCookie.getName();
            if(sname.equals(name))
            {
                svalue=sCookie.getValue();
                break;
            }
        }
        return svalue;
    }


	@Override
	public boolean isProtectedURI(String arg0) {
		return false;
	}

}
