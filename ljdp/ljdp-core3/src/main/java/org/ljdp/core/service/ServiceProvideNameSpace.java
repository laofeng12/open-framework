package org.ljdp.core.service;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.namespace.NameNotFoundException;
import org.ljdp.component.namespace.NameSpaceManager;

/**
 * 服务提供者的查找器。
 * 
 * @author hzy
 *
 */
public class ServiceProvideNameSpace implements NameSpaceManager {

	/**
	 * 默认的SP查找器,查找以Service结尾的服务接口的实现类,实现类名替换Service为SP.
	 * 如服务接口为CommonService,则查找CommonSP.
	 */
	@SuppressWarnings("rawtypes")
	public String classNameing(Class entity, String name)
			throws NameNotFoundException {
		if(StringUtils.isBlank(name)) {
			name = "default";
		}
		if(name.equalsIgnoreCase("default")) {
			String spName = entity.getName();
			spName = spName.substring(0, spName.lastIndexOf("Service"));
			spName += "SP";
			return spName;
		}
		throw new NameNotFoundException(name);
	}

	/**
	 * 从参数所指的服务接口查找实现此接口的服务提供者.
	 */
	@SuppressWarnings("rawtypes")
	public Class classSeek(Class entity, String name)
			throws NameNotFoundException, ClassNotFoundException {
		String className = classNameing(entity, name);
		return Class.forName(className);
	}

}
