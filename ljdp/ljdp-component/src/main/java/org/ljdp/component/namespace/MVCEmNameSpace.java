package org.ljdp.component.namespace;

/**
 * 实体模块的命名空间
 * @author hzy
 *
 */
public class MVCEmNameSpace implements NameSpaceManager {
	private String module = "persistent";
	private String view = "web";
	private String control = "service";
	private String business = "business";
	
	private String webParamSuffix = "WebParam";
	private String dbParamSuffix = "DBParam";
	private String webFormSuffix = "Form";
	private String dbServiceSuffix = "Service";
	private String dbDaoSuffix = "DAO";
	private String webBusiSuffix = "BO";
	
	private String getNameSapce(Class<Object> table) {
		String voName = table.getName();
		String packName = voName.substring(0, voName.lastIndexOf("."+module+"."));
		return packName;
	}
	
	/**
	 * 取实体Web查询参数对象名称
	 * @param entity
	 * @return
	 */
	public String getWebParam(Class<Object> entity) {
		String webName = getNameSapce(entity);
		webName += "."+view+"." + entity.getSimpleName() + webParamSuffix;
		return webName;
	}
	
	/**
	 * 取实体Web数据传输对象名称
	 * @param entity
	 * @return
	 */
	public String getWebForm(Class<Object> entity) {
		String formName = getNameSapce(entity);
		formName += "."+view+"." + entity.getSimpleName() + webFormSuffix;
		return formName;
	}
	
	/**
	 * 取实体数据库查询参数对象名称
	 * @param entity
	 * @return
	 */
	public String getDBParam(Class<Object> entity) {
		String paramName = entity.getName() + dbParamSuffix;
		return paramName;
	}
	
	public String getDBDAO(Class<Object> entity) {
		String paramName = entity.getName() + dbDaoSuffix;
		return paramName;
	}
	
	/**
	 * 取实体数据库服务对象名称
	 * @param entity
	 * @return
	 */
	public String getDBService(Class<Object> entity) {
		String servName = getNameSapce(entity);
		servName += "."+control+"." + entity.getSimpleName() + dbServiceSuffix;
		return servName;
	}
	
	/**
	 * 获得业务类名
	 * @param entity
	 * @param name
	 * @return
	 */
	public String getWebBusiness(Class<Object> entity, String name) {
		String ns = getNameSapce(entity);
		String clsName = ns + "." + business + "." + name + webBusiSuffix;
		return clsName;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String classNameing(Class entity, String name) throws NameNotFoundException{
		if(name.equals("web.param")) {
			return getWebParam(entity);
		} else if(name.equals("web.form")) {
			return getWebForm(entity);
		} else if(name.equals("db.param")) {
			return getDBParam(entity);
		} else if(name.equals("db.service")) {
			return getDBService(entity);
		} else if(name.equals("db.dao")) {
			return getDBDAO(entity);
		} else if(name.startsWith("web.business")) {
			String[] items = name.split("=");
			return getWebBusiness(entity, items[1]);
		}
		throw new NameNotFoundException("the Name "+name+" is not support!");
	}
	
	@SuppressWarnings("rawtypes")
	public Class classSeek(Class entity, String name) throws NameNotFoundException, ClassNotFoundException{
		String clsName = classNameing(entity, name);
		Class cls = Class.forName(clsName);
		return cls;
	}
}
