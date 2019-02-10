package org.ljdp.ui.struts2;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.ConfigFileFactory;
import org.ljdp.common.config.Constant;
import org.ljdp.common.config.Env;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.namespace.NameSpaceManager;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.component.strategy.BusinessObject;
import org.ljdp.component.transform.MapStringTransformer;
import org.ljdp.component.transform.StringTransformer;
import org.ljdp.component.user.DBAccessUser;
import org.ljdp.component.user.UserProvider;
import org.ljdp.component.user.UserRoleChecker;
import org.ljdp.core.db.DBQueryParam;
import org.ljdp.core.db.DataPackage;
import org.ljdp.core.query.RO;
import org.ljdp.core.service.CommonService;
import org.ljdp.core.service.ServiceFactory;
import org.ljdp.ui.common.FieldTranslator;
import org.ljdp.ui.common.WebConstant;
import org.ljdp.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public abstract class BaseAction extends ServletSupportAction {
	private static final long serialVersionUID = 817083538729470753L;
	private Logger log = LoggerFactory.getLogger(BaseAction.class);

	public static final int METHOD_TYPE_QUERY = 0;
	public static final int METHOD_TYPE_CREATE = 1;
	public static final int METHOD_TYPE_UPDATE = 2;
	public static final int METHOD_TYPE_REMOVEBYPK = 3;
	public static final int METHOD_TYPE_REMOVEBYVO = 4;
	public static final int METHOD_TYPE_FINDBYPK = 5;
	public static final int METHOD_TYPE_QUERYALL = 6;
	public static final int METHOD_TYPE_QUERYDATA = 7;

	private static final Class[] METHOD_PARAM_QUERY = { DBQueryParam.class };
	private static final Class[] METHOD_PARAM_PK = { Serializable.class };
	private static final Class[] METHOD_PARAM_CRUD = { Serializable.class };

	// 保存页面的数据
	private DBQueryParam param;
	private Object form;

	// 关联的库表--必需设置此属性
	private Class entity;
	// 表的主键集
	private String[] pkNames;
	//主键对象名
	private String pkObjectName;
	
	// 访问表的服务接口
	private Class service;
	// 查询数据库表的参数
	private DBQueryParam dbParam;

	private NameSpaceManager emNameSpace;

	// 查询后得到的结果集
	private DataPackage dp;
	//新增操作还是修改操作
	private Boolean isNew;
	//是否可编辑
	private boolean editAble;

	// 默认操作都成功
	private Result result = new GeneralResult(true);

	//数据库查询后是否触发事件
	private boolean afterDBQuery = true;
	//是否自动触发翻译
	private boolean translate = true;
	//字段翻译器
	private List<FieldTranslator> fieldTranslators;
	//限制查询的总数据量，防止全表查询导致内存溢出
	private Boolean restrictQueryQuantity = true;

	public BaseAction(Class table, String[] pkNames) {
		super();
		this.entity = table;
		this.pkNames = pkNames;
	}

	public BaseAction() {
		super();
	}

	public NameSpaceManager getEmNameSpace() throws ClassNotFoundException {
		if (emNameSpace == null) {
			try {
				setEmNameSpaceBySpring("system.module.NameSpaceManager");
			} catch (ClassNotFoundException e) {
				throw e;
			}
		}
		return emNameSpace;
	}

	private void setEmNameSpaceBySpring(String beanName)
			throws ClassNotFoundException {
		if (SpringContextManager.containsBean(beanName)) {
			emNameSpace = (NameSpaceManager) SpringContextManager
					.getBean(beanName);
			if (log.isDebugEnabled()) {
				log.debug(beanName + "=" + emNameSpace);
			}
		} else {
			String msg = "The Spring bean " + beanName + " is not Found";
			throw new ClassNotFoundException(msg);
		}
	}

	public void setEmNameSpace(String beanname) {
		try {
			setEmNameSpaceBySpring(beanname);
		} catch (ClassNotFoundException e) {
			try {
				emNameSpace = (NameSpaceManager) Class.forName(beanname)
						.newInstance();
			} catch (Exception e1) {

			}
		}
	}

	public DBQueryParam getParam() {
		if (null == param) {
			try {
				if(getEntity() != null) {
					Class webParamCls = getEmNameSpace().classSeek(getEntity(),
					"web.param");
					param = (DBQueryParam) webParamCls.newInstance();
				} else {
					param = getDbParam();
				}
			} catch (Exception e) {
				param = getDbParam();
			}
		}
		return param;
	}

	public void setParam(DBQueryParam param) {
		this.param = param;
	}

	public Object getForm() {
		if (null == form) {
			try {
				Class formCls = getEmNameSpace().classSeek(getEntity(),
						"web.form");
				form = formCls.newInstance();
			} catch (Exception e1) {
				try {
					form = getEntity().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return form;
	}

	public void setForm(Object form) {
		this.form = getForm();
		try {
			PropertyUtils.copyProperties(this.form, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getPkNames() {
		return pkNames;
	}

	public void setPkNames(String[] pkNames) {
		this.pkNames = pkNames;
		if(pkNames[0].indexOf(".") > 0) {
			this.pkObjectName = pkNames[0].substring(0, pkNames[0].indexOf("."));
		}
	}

	public String getResultMsg() {
		if (result.getMsg() != null) {
			ConfigFile cfg = ConfigFileFactory.getInstance().getAppConfig();
			String template = cfg.getValue("page.tip.template");
			return MessageFormat.format(template, new Object[] { result
					.getMsg() });
		}
		return result.getMsg();
	}

	public void setResultMsg(String resultmsg) {
		result.setMsg(resultmsg);
	}

	public Class getEntity() {
		return entity;
	}

	public void setEntity(Class table) {
		this.entity = table;
	}

	public Class getService() {
		if (null == service) {
			try {
				service = getEmNameSpace().classSeek(getEntity(), "db.service");
			} catch (Exception e) {
				service = CommonService.class;
			}
		}
		return service;
	}

	public void setService(Class service) {
		this.service = service;
	}

	public DBQueryParam getDbParam() {
		if (dbParam == null) {
			try {
				if(getEntity() != null) {
					Class dbCls = getEmNameSpace().classSeek(getEntity(),
					"db.param");
					dbParam = (DBQueryParam) dbCls.newInstance();
				} else {
					dbParam = new DBQueryParam();
				}
			} catch (Exception e) {
				dbParam = new DBQueryParam();
			}
		}
		return dbParam;
	}

	public void setDbParam(DBQueryParam dbParam) {
		this.dbParam = dbParam;
	}

	public DataPackage getDp() {
		return dp;
	}

	public void setDp(DataPackage dp) {
		this.dp = dp;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public DBAccessUser getUser() {
		String upid = "web.UserProvider";
		if (Env.current().runstatus().contains(Constant.Run.NOSERVER)) {
			upid = "system.UserProvider";
		}
		if(Env.current().runstatus().contains(Constant.Run.NOLOGIN)) {
			upid = "web.visitor.UserProvider";
		}
		UserProvider userProvider = (UserProvider) SpringContextManager
				.getBean(upid);
		return userProvider.getUser();
	}
	
	public boolean isAdmin() {
		UserRoleChecker checker = (UserRoleChecker)SpringContextManager.getBean("system.UserRoleChecker");
		if(checker != null) {
			return checker.isAdministrator(getUser());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected Object executeService(int type, Object arg) throws Exception {
		String methodName = null;
		Class[] methodType = null;
		switch (type) {
		case METHOD_TYPE_QUERY:
			methodName = "doQuery";
			methodType = METHOD_PARAM_QUERY;
			break;
		case METHOD_TYPE_CREATE:
			methodName = "doCreate";
			methodType = METHOD_PARAM_CRUD;
			break;
		case METHOD_TYPE_UPDATE:
			methodName = "doUpdate";
			methodType = METHOD_PARAM_CRUD;
			break;
		case METHOD_TYPE_REMOVEBYPK:
			methodName = "doRemoveByPK";
			methodType = METHOD_PARAM_PK;
			break;
		case METHOD_TYPE_REMOVEBYVO:
			methodName = "doRemove";
			methodType = METHOD_PARAM_PK;
			break;
		case METHOD_TYPE_FINDBYPK:
			methodName = "doFindByPK";
			methodType = METHOD_PARAM_PK;
			break;
		case METHOD_TYPE_QUERYALL:
			methodName = "doQueryAll";
			methodType = METHOD_PARAM_QUERY;
			break;
		case METHOD_TYPE_QUERYDATA:
			methodName = "doQueryData";
			methodType = METHOD_PARAM_QUERY;
			break;
		}

		CommonService sp;
		if (getService().equals(CommonService.class)) {
			sp = ServiceFactory.buildCommon(entity);
		} else {
			sp = (CommonService) ServiceFactory.build(getService());
			if (null == sp.getEntityClass()) {
				sp.setEntityClass(getEntity());
			}
			if (null == sp.getDaoClass()) {
				try {
					Class daoClass = getEmNameSpace().classSeek(getEntity(),
							"db.dao");
					sp.setDaoClass(daoClass);
				} catch (Exception e) {
				}
			}
		}
		Method method = sp.getClass().getMethod(methodName, methodType);
		Object rlt = null;
		try {
			rlt = method.invoke(sp, new Object[] { arg });
		} catch (Throwable e) {
			String msg = "";
			if (e instanceof InvocationTargetException) {
				InvocationTargetException ie = (InvocationTargetException) e;
				Throwable t = ie.getTargetException();
				t.printStackTrace();
				msg = t.getMessage() != null ? t.getMessage() : t.toString();
			} else {
				msg = e.getMessage() != null ? e.getMessage() : e.toString();
			}
			setResultMsg(msg);
			while (e.getCause() != null) {
				e = e.getCause();
			}
			throw (Exception) e;
		}
		return rlt;
	}

	protected Object findVOFromDB() throws Exception {
		onBeforeDBQuery();
		String pkValues = getParam().get_pk();
		if (StringUtils.isEmpty(pkValues)) {
			throw new Exception("主键值空");
		}
		Object contentVO;
		if (pkValues.indexOf("|") == -1) { // 单一主键
			contentVO = executeService(METHOD_TYPE_FINDBYPK, getSelectedPK(pkValues));
		} else { // 复合主键
			if(StringUtils.isNotEmpty(pkObjectName)) {
				//组合成对象的复合主键
				contentVO = executeService(METHOD_TYPE_FINDBYPK,
						getSelectedPkObject(pkValues));
			} else {
				contentVO = executeService(METHOD_TYPE_FINDBYPK,
						getSelectedPkVO(pkValues));
			}
		}
		return contentVO;
	}

	protected Serializable getSelectedPK(String pkValue) throws Exception {
		if (pkNames == null || pkNames.length == 0
				|| StringUtils.isEmpty(pkNames[0])) {
			throw new Exception("未设置" + entity.getSimpleName() + "的主键");
		}
		Method[] methodArray = entity.getMethods();
		Class pkType = null;
		for (int i = 0; i < methodArray.length; i++) {
			if (methodArray[i].getName().equalsIgnoreCase("get" + pkNames[0])) {
				pkType = methodArray[i].getReturnType();
			}
		}
		if (Integer.class == pkType) {
			return new Integer(pkValue);
		} else if (Long.class == pkType) {
			return new Long(pkValue);
		} else if (String.class == pkType) {
			return pkValue;
		} else {
			throw new Exception("不正确的主键类型");
		}
	}

	protected Serializable getSelectedPkVO(String pkValue) throws Exception {
		if (pkNames == null || pkNames.length == 0) {
			throw new Exception("未设置" + entity.getSimpleName() + "的主键");
		}
		String[] pkValueArray = pkValue.split("\\|");
		Serializable vo = (Serializable) entity.newInstance();
		for (int j = 0; j < pkValueArray.length; j++) {
			Type fType = entity.getDeclaredField(pkNames[j]).getGenericType();
			Class fCls = Class.forName(fType.toString().replaceFirst("class ", ""));
			Constructor fCst = fCls.getConstructor(String.class);
			Object fValue = fCst.newInstance(pkValueArray[j]);
			PropertyUtils.setProperty(vo, pkNames[j], fValue);
		}
		return vo;
	}
	
	protected Serializable getSelectedPkObject(String pkValue) throws Exception {
		if(pkObjectName == null) {
			throw new Exception("未设置" + entity.getSimpleName() + "的主键");
		}
		String[] pkValueArray = pkValue.split("\\|");
		Type pkType = entity.getDeclaredField(pkObjectName).getGenericType();
		Class pkCls = Class.forName(pkType.toString().replaceFirst("class ", ""));
		Constructor[] csItems = pkCls.getConstructors();//主键对象的所有构造器
		Constructor mainCS = null;//参数数量和主键数一样的构造器
		for (int i = 0; i < csItems.length; i++) {
			if(csItems[i].getParameterTypes().length == 0) {
				//优先使用无参数构造器
				mainCS = null;
				break;
			}
			if(csItems[i].getParameterTypes().length == pkNames.length) {
				mainCS = csItems[i];
			}
		}
		Serializable pkObj = null;
		if(mainCS == null) {
			//无参数构造器
			pkObj = (Serializable) pkCls.newInstance();
			for (int i = 0; i < pkValueArray.length; i++) {
				String fName = pkNames[i].substring(pkNames[i].indexOf(".")+1);
				Type fType = pkCls.getDeclaredField(fName).getGenericType();
				Class fCls = Class.forName(fType.toString().replaceFirst("class ", ""));
				Constructor fCst = fCls.getConstructor(String.class);
				Object fValue = fCst.newInstance(pkValueArray[i]);
				PropertyUtils.setProperty(pkObj, fName, fValue);
			}
		} else {
			//有参数构造器
			//注意：pkValue参数的顺序必需和构造器参数的顺序一致！
			Class[] paramCls = mainCS.getParameterTypes();//定义的参数列表
			Object[] csParams = new Object[paramCls.length];//参数列表
			for (int i = 0; i < paramCls.length; i++) {
				Constructor paramCs = paramCls[i].getConstructor(String.class);
				csParams[i] = paramCs.newInstance(pkValueArray[i]);
			}
			pkObj = (Serializable) mainCS.newInstance(csParams);
		}
		
		return pkObj;
	}

	protected void onDuplicatePk() {
		this.setResultMsg("重复主键");
		this.result.setSuccess(false);
	}

	public String doShow() throws Exception {
		setForm(findVOFromDB());
		this.setIsNew(false);
		setEditAble(false);
		if (isAfterDBQuery()) {
			onFormLoad();
		}
		return WebConstant.RESULT_CONTENT;
	}

	/**
	 * 事件：在Form成功从数据库装载后，处理Form中的数据，如进行数据转换或翻译之类的。
	 */
	protected void onFormLoad() {
		if(isTranslate()) {
			translateBean(getForm());
		}
	}

	/**
	 * 事件：在doList查询数据库前运行
	 */
	protected void onBeforeList() {

	}

	/**
	 * 事件：在查询数据库前运行
	 */
	protected void onBeforeDBQuery() {

	}

	/**
	 * 事件：对查询后的数据进行些处理，通常是做翻译
	 * 
	 * @param list
	 */
	protected void onAfterDBQuery() {
		if(isTranslate()) {
			if(!getDp().isEmpty() && this.fieldTranslators != null) {
				Iterator it = getDp().getDatas().iterator();
				while(it.hasNext()) {
					Object bean = it.next();
					translateBean(bean);
				}
			}
		}
	}

	protected void translateBean(Object bean) {
		if(this.fieldTranslators == null) {
			return;
		}
		try {
			for (int i = 0; i < fieldTranslators.size(); i++) {
				FieldTranslator ft = fieldTranslators.get(i);
				Object value = PropertyUtils.getSimpleProperty(bean, ft.getCodeField());
				String transVal = ft.getTransformer().transform(value);
				PropertyUtils.setSimpleProperty(bean, ft.getNameField(), transVal);
			}
		} catch (Exception e) {
			log.error("翻译时发生异常", e);
		}
	}
	
	/**
	 * 事件：从数据库删除前运行，返回true才删除
	 * @return
	 */
	protected boolean onBeforeDelete() {
		return true;
	}
	
	/**
	 * 事件：成功从数据库删除后运行.
	 */
	protected void onDeleteSuccess() {
		
	}

	public String doList() throws Exception {
		onBeforeList();
		if (!getParam().equals(getDbParam())) {
			PropertyUtils.copyProperties(getDbParam(), getParam());
		}
		onBeforeDBQuery();
		restrictQuery();
		DataPackage dp = (DataPackage) executeService(METHOD_TYPE_QUERY,
				getDbParam());
		setDp(dp);
		if (isAfterDBQuery()) {
			onAfterDBQuery();
		}
		return WebConstant.RESULT_LIST;
	}
	
	public String doListData() throws Exception {
		onBeforeList();
		if (!getParam().equals(getDbParam())) {
			PropertyUtils.copyProperties(getDbParam(), getParam());
		}
		onBeforeDBQuery();
		restrictQuery();
		DataPackage dp = (DataPackage) executeService(METHOD_TYPE_QUERYDATA,
				getDbParam());
		setDp(dp);
		if (isAfterDBQuery()) {
			onAfterDBQuery();
		}
		return WebConstant.RESULT_LIST;
	}

	/**
	 * 默认最多只装载1000条数据到内存
	 */
	private void restrictQuery() {
		if(restrictQueryQuantity) {
			getParam().setRestrictQueryQuantity(true);
			if(getParam().get_pagesize() > 10000) {
				getParam().set_pagesize("10000");
			}
		}
	}

	public String doListExpress() throws Exception {
		getParam().set_pageno("1");
		getParam().set_pagesize("100");
		return doList();
	}

	public String doListAll() throws Exception {
		onBeforeList();
		if (!getParam().equals(getDbParam())) {
			PropertyUtils.copyProperties(getDbParam(), getParam());
		}
		onBeforeDBQuery();
		restrictQuery();
		DataPackage dp = (DataPackage) executeService(METHOD_TYPE_QUERYALL,
				getDbParam());
		setDp(dp);
		if (isAfterDBQuery()) {
			onAfterDBQuery();
		}
		return WebConstant.RESULT_LIST;
	}
	
	public void doDeleteHandle() throws Exception {
		if(onBeforeDelete()) {
			processDelete();
			onDeleteSuccess();
		}
	}

	public String doDelete() {
		try {
			doDeleteHandle();
			return doList();
		} catch (Exception e) {
			addActionError(e.getMessage());
			return WebConstant.RESULT_LIST;
		}
	}

	protected void processDelete() throws Exception {
		String[] selectArray = getParam().get_selectItems();
		if (selectArray == null) {
			throw new Exception("无法获取选中项目！");
		}
		for (int i = 0; i < selectArray.length; i++) {
			if (selectArray[0].indexOf('|') == -1) { // 单一主键
				executeService(METHOD_TYPE_REMOVEBYPK,
						getSelectedPK(selectArray[i]));
			} else { // 复合主键
				if(StringUtils.isNotEmpty(pkObjectName)) {
					//组合成对象的复合主键
					executeService(METHOD_TYPE_REMOVEBYPK,
							getSelectedPkObject(selectArray[i]));
				} else {
					getParam().set_pk(selectArray[i]);
					Object vo = findVOFromDB();
					executeService(METHOD_TYPE_REMOVEBYVO, vo);
				}
			}
		}
	}

	public String doNew() {
		try {
			// 新建时设置form的默认值
			setForm(entity.newInstance());
			this.setIsNew(true);
			setEditAble(true);
		} catch (Exception e) {
			addActionError(e.getMessage());
		}
		return WebConstant.RESULT_CONTENT;
	};

	public String doEdit() throws Exception {
		setForm(findVOFromDB());
		this.setIsNew(false);
		setEditAble(true);
		onFormLoad();
		return WebConstant.RESULT_CONTENT;
	};
	
	/**
	 * 保存（新增/更新）前运行。
	 * 常见使用场景：
	 * 1、设置主键值
	 * 2、判断是新增还是更新操作
	 * 3、检查字段
	 * 
	 * 返回false则 不保存
	 */
	protected boolean onBeforeSave() {
		return true;
	}
	
	/**
	 * 成功保存后运行
	 */
	protected void onSaveSuccess(Object bean) {}
	/**
	 * 保存失败后运行
	 */
	protected void onSaveFailure(Object bean) {}
	
	/**
	 * 保存后运行
	 */
	protected void onAfterSave() {}

	/**
	 * 单表数据库保存，支持新增和更新操作
	 * @return
	 * @throws Exception
	 */
	public String doSave() throws Exception {
		if (pkNames == null || pkNames.length == 0) {
			throw new Exception("未设置主键");
		}
		if(getIsNew() == null) {
			//自动判断是新增还是更新操作：通过主键判断
			//判断主键是否为空，空的话是新增操作，不空的话是更新操作
			setIsNew(false);
			for (int i = 0; i < pkNames.length; i++) {
				Object pk = PropertyUtils.getProperty(getForm(), pkNames[i]);
				if(pk == null) {
					setIsNew(true);
					break;
				}
			}
		}
		if(!onBeforeSave()) {
			getResult().setSuccess(false);
			getResult().setMsg("前置条件检查不通过");
			setIsNew(false);
			setEditAble(false);
			return WebConstant.RESULT_CONTENT;
		}
		Object obj = entity.newInstance();
		PropertyUtils.copyProperties(obj, getForm());
		Object vo = null;
		if (pkNames.length == 1) { // 单一主键
			Object pk = PropertyUtils.getProperty(obj, pkNames[0]);
			if (pk != null) {
				vo = executeService(METHOD_TYPE_FINDBYPK, (Serializable) pk);
			}
		} else {// 复合主建
			if(StringUtils.isNotEmpty(pkObjectName)) {
				//使用单一主键对象的复合主键
				Object pk = PropertyUtils.getProperty(obj, pkObjectName);
				if (pk != null) {
					vo = executeService(METHOD_TYPE_FINDBYPK, (Serializable) pk);
				}
			} else {
				vo = executeService(METHOD_TYPE_FINDBYPK, (Serializable) obj);
			}
		}
		if (getIsNew()) {
			if (vo == null) {
				executeService(METHOD_TYPE_CREATE, obj);
				this.setResultMsg("保存成功");
			} else {
				onDuplicatePk();
			}
		} else {
			if (vo != null) {
				MyBeanUtils.copyPropertiesOfNull(obj, vo);
				executeService(METHOD_TYPE_UPDATE, obj);
				this.setResultMsg("更新成功");
			} else {
				setResultMsg("找不到数据");
				result.setSuccess(false);
			}
		}
		if(getResult().isSuccess()) {
			onSaveSuccess(obj);
		} else {
			onSaveFailure(obj);
		}
		onAfterSave();
		setIsNew(false);
		setEditAble(false);
		return WebConstant.RESULT_CONTENT;
	};

	/**
	 * 搜索当前记录（节点）的所有子记录（节点）。
	 * 
	 * @param node
	 * @param parentColumn
	 *            表示父节点的字段
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected Collection<Object> depthSearch(Object node, String parentColumn)
			throws Exception {
		Object pk = PropertyUtils.getSimpleProperty(node, getPkNames()[0]);
		DBQueryParam param = new DBQueryParam();
		param.addQueryCondition(parentColumn, RO.EQ, pk);
		Collection<Object> mylist = queryAll(param);
		ArrayList<Object> childs = new ArrayList<Object>();
		Iterator<Object> it = mylist.iterator();
		while (it.hasNext()) {
			Collection<Object> thelist = depthSearch(it.next(), parentColumn);
			childs.addAll(thelist);
		}
		mylist.addAll(childs);
		return mylist;
	}

	protected Collection queryAll(DBQueryParam params) throws Exception {
		DataPackage dp = (DataPackage) executeService(METHOD_TYPE_QUERYALL,
				params);
		return dp.getDatas();
	}

	/**
	 * 通用业务处理。 例如访问路径为：/business_xxx.do，则查找XxxWBO.class进行处理，
	 * XxxWBO.class必须实行接口BusinessObject
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doBusiness() {
		try {
			String path = getCommandFromPath(getRequest().getRequestURI());
			if (path.indexOf("_") == -1) {
				throw new Exception(
						"not support this operation, the Correct method is business_*.do");
			}
			String[] items = path.split("_");
			if (items.length == 1) {
				throw new Exception(
						"not support this operation, the Correct method is business_*.do");
			}
			String[] myitems = items[1].split("\\.");
			String busiOper = myitems[0];
			busiOper = NameUtils.toUpperCaseFirst(busiOper);
			Class busiCls = getEmNameSpace().classSeek(getEntity(),
					"web.business=" + busiOper);
			BusinessObject bo = (BusinessObject) busiCls.newInstance();
			Object[] params = getWebParams();
			Object res = bo.doBusiness(params);
			if (res != null) {
				String result = res.toString();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Object[] getWebParams() {
		Object[] params = { getRequest(), getResponse(), getSession(),
				getUser(), this };
		return params;
	}

	protected String getCommandFromPath(String path) {
		if (path == null) {
			return path;
		}

		int i = path.lastIndexOf("/");
		if (i < 0) {
			return path;
		} else {
			return path.substring(i + 1);
		}
	}
	
	public String getRequestCommand() {
		return getCommandFromPath(getRequest().getRequestURI());
	}

	public Result getResult() {
		return result;
	}

	/**
	 * 数据库查询后是否触发事件
	 * @return
	 */
	public boolean isAfterDBQuery() {
		return afterDBQuery;
	}

	/**
	 * 数据库查询后是否触发事件
	 * @param proceAfterQuery
	 */
	public void setAfterDBQuery(boolean proceAfterQuery) {
		this.afterDBQuery = proceAfterQuery;
	}

	public void setEmNameSpace(NameSpaceManager emNameSpace) {
		this.emNameSpace = emNameSpace;
	}

	public boolean getEditAble() {
		return editAble;
	}

	public void setEditAble(boolean editAble) {
		this.editAble = editAble;
	}
	
	/**
	 * 增加翻译器
	 * @param codeField 待翻译的字段
	 * @param nameField 翻译后值存放字段
	 * @param transformer 转换器
	 */
	public void addTranslator(String codeField, String nameField, StringTransformer transformer) {
		if(transformer == null) {
			return;
		}
		if(this.fieldTranslators == null) {
			this.fieldTranslators = new ArrayList<FieldTranslator>();
		}
		this.fieldTranslators.add(new FieldTranslator(codeField, nameField, transformer));
	}
	
	/**
	 * 增加翻译器
	 * @param codeField 待翻译的字段
	 * @param nameField 翻译后值存放字段
	 * @param map<String,String> 翻译字典
	 */
	public void addTranslator(String codeField, String nameField, Map<String, String> map) {
		addTranslator(codeField, nameField, new MapStringTransformer(map));
	}

	public List<FieldTranslator> getFieldTranslators() {
		return fieldTranslators;
	}

	public void setFieldTranslators(List<FieldTranslator> fieldTranslators) {
		this.fieldTranslators = fieldTranslators;
	}

	public boolean isTranslate() {
		return translate;
	}

	public void setTranslate(boolean translate) {
		this.translate = translate;
	}

	protected Boolean getRestrictQueryQuantity() {
		return restrictQueryQuantity;
	}

	protected void setRestrictQueryQuantity(Boolean restrictQueryQuantity) {
		this.restrictQueryQuantity = restrictQueryQuantity;
	}
}
