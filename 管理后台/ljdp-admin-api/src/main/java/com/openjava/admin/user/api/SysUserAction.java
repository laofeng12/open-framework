package com.openjava.admin.user.api;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.FieldType;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.config.ConfigFile;
import org.ljdp.common.config.Env;
import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.common.spring.SpringContextManager;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.plugin.sys.resp.LoginResp;
import org.ljdp.plugin.sys.vo.OrgVO;
import org.ljdp.plugin.sys.vo.RoleVO;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.cipher.MD5;
import org.ljdp.secure.cipher.SHA256;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.support.dictionary.DictConstants;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.user.component.UserInitialize;
import com.openjava.admin.user.domain.SysUser;
import com.openjava.admin.user.service.SysUserOrgService;
import com.openjava.admin.user.service.SysUserRoleService;
import com.openjava.admin.user.service.SysUserService;
import com.openjava.framework.sys.domain.LmMemberToken;
import com.openjava.framework.sys.service.LmMemberTokenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import com.openjava.admin.user.query.SysUserDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="用户管理")
@RestController
@RequestMapping("/admin/user/sysUser")
public class SysUserAction {
	
	@Resource
	private SysUserService sysUserService;
	@Resource
	private SysUserRoleService sysUserRoleService;
	@Resource
	private SysUserOrgService sysUserOrgService;
	@Resource
	private LmMemberTokenService lmMemberTokenService;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysUser get(@PathVariable("id")Long id) throws APIException{
		SysUser m = sysUserService.get(id);
		if(m == null) {
			throw new APIException(404, "数据不存在");
		}
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_fullname", value = "名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_account", value = "登录账号=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_isexpired", value = "是否过期=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_islock", value = "是否锁定=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "le_createtime", value = "创建时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_createtime", value = "创建时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "eq_status", value = "状态=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_mobile", value = "手机号码=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_fromtype", value = "来源类型=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysUser> doSearch(SysUserDBParam params, Pageable pageable){
		if(StringUtils.isNotBlank(params.getLike_fullname())) {
			params.setLike_fullname("%"+params.getLike_fullname()+"%");
		}
		Page<SysUser> result =  sysUserService.query(params, pageable);
		
//		BaseVO.setKey(result.getContent(), "userid");//为每条数据设置一个唯一key值，主要在react中使用，如果对接其他前端可以省略
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userid", value = "用户id", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "fullname", value = "名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "accounttype", value = "帐号类型(SYS.AccountType)", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "account", value = "登录账号", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "isexpired", value = "是否过期", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "islock", value = "是否锁定", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "createtime", value = "创建时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "email", value = "邮箱", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "mobile", value = "手机号码", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "picture", value = "头像", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "fromtype", value = "来源类型", required = false, dataType = "Short", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysUser model, @RequestParam("isNew")Boolean isNew

			) throws Exception{
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setUserid(ss.getSequence());
			if(StringUtils.isNotBlank(model.getPassword())) {
				String s = SHA256.encodeAsBase64(model.getPassword());
				model.setPassword(s);
			}
			model.setCreatetime(new Date());
			sysUserService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysUser db = sysUserService.get(model.getUserid());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			if(StringUtils.isNotBlank(model.getPassword())) {
				String s = SHA256.encodeAsBase64(model.getPassword());
				db.setPassword(s);
			}
			sysUserService.doSave(db);
		}
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysUserService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "批量删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public ApiResponse doRemove(@RequestParam("ids")String ids) {
		sysUserService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "登录", nickname="login")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userAccount", value = "用户账号", required = true, paramType = "post"),
		@ApiImplicitParam(name = "userPwd", value = "密码", required = true, paramType = "post"),
		@ApiImplicitParam(name = "userAgent", value = "用户浏览器描述", required = false, paramType = "post"),
		@ApiImplicitParam(name = "authCode", value = "验证码", required = false, paramType = "post"),
		@ApiImplicitParam(name = "devType", value = "设备类型（app登录必须传）", required = false, paramType = "post")
	})
	@Security(session=false)
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public LoginResp login(HttpServletRequest request
			,@RequestParam("userAccount")String userAccount
			,@RequestParam("userPwd")String userPwd
			,@RequestParam(value="userAgent",required=false)String userAgent
			,@RequestParam(value="authCode",required=false)String authCode
			,@RequestParam(value="devType",required=false)String devType) throws Exception{
		if(userAgent == null) {
			userAgent = request.getHeader("user-agent");
		}
		if(userAgent == null) {
			throw new APIException(1001, "缺少userAgent");
		}
		SysUser u = sysUserService.findByAccount(userAccount);
		if(u == null) {
			throw new APIException(1002, "用户名或密码不正确");
		}
		if(!u.getPassword().equals(SHA256.encodeAsBase64(userPwd))) {
			if(!u.getPassword().equals(SHA256.encodeAsString(userPwd))) {
				if(!u.getPassword().equals(MD5.encodeAsString(userPwd))) {
					throw new APIException(1002, "用户名或密码不正确");
				}
			}
		}
		if(u.getStatus().shortValue() != 1) {
			throw new APIException(1003, "用户已停用");
		}
		if(u.getIslock().shortValue() == 1) {
			throw new APIException(1004, "用户已锁定");
		}
		if(u.getIsexpired().shortValue() == 1) {
			throw new APIException(1005, "用户已过期");
		}
		
		List<SysRole> roles = sysUserRoleService.findRoleByUser(u.getUserid());
		if(roles.isEmpty()) {
			throw new APIException(1006, "用户无权限");
		}
		
		List<RoleVO> roleList = new ArrayList<>();
		roles.forEach(sr -> {
			RoleVO v = new RoleVO();
			v.setRoleId(sr.getRoleid().toString());
			v.setRoleName(sr.getRolename());
			v.setRoleAlias(sr.getAlias());
			roleList.add(v);
		});

		List<SysOrg> orgs = sysUserOrgService.findOrgByUser(u.getUserid());
		List<OrgVO> orgList = new ArrayList<>();
		orgs.forEach(o ->{
			OrgVO v = new OrgVO();
			v.setOrgId(o.getOrgid());
			v.setOrgName(o.getOrgname());
			v.setParentId(o.getOrgsupid());
			orgList.add(v);
		});
		
		UserVO vo = new UserVO();
		vo.setUserId(u.getUserid().toString());
		vo.setUserAccount(u.getAccount());
		vo.setUserName(u.getFullname());
		vo.setUserMobileno(u.getMobile());
		vo.setHeadImg(u.getPicture());
		vo.setUserType(u.getAccounttype());
		vo.setLoginTime(new Date());
		vo.setRefreshTime(new Date());
		vo.setUserAgent(userAgent);
		vo.setRoleList(roleList);
		vo.setOrgList(orgList);
		
		//扩展，给具体的项目系统初始化自己需要的数据
		try {
			UserInitialize ui = (UserInitialize)SpringContextManager.getBean("userInitialize");
			if(ui != null) {
				BaseUserInfo newUser = ui.onLogin(vo, null);
				if(newUser != null) {
					vo = (UserVO)newUser;
				}
			}
		} catch (NoSuchBeanDefinitionException e) {
		}
		
		//会话保持时间
		ConfigFile mycfg = Env.getCurrent().getConfigFile();
		String loginTimeoutCfg = mycfg.getValue("session.login.timeout.min");
		int loginTimeout = 120;
		if(StringUtils.isNotEmpty(loginTimeoutCfg)) {
    		try {
    			loginTimeout = Integer.parseInt(loginTimeoutCfg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		vo.setExpireInMin(loginTimeout);
		//会话缓存类型
		String sessionType = mycfg.getValue("session.cache");
		if(StringUtils.isEmpty(loginTimeoutCfg)) {
			sessionType = "redis";
		}
		
		String tokenId = lmMemberTokenService.createToken(u.getUserid(), userAgent);
		if(sessionType.equalsIgnoreCase("ehcache")) {
			//单机版，在jvm缓存session
			MemoryCache.putData(DictConstants.CACHE_SESSION, tokenId, vo);
		} else {
			//集群版,默认用redis
			redisTemplate.boundValueOps(tokenId).set(vo, loginTimeout, TimeUnit.MINUTES);
		}
		if(StringUtils.isNotBlank(devType)) {
			//app登录，把session持久化，保持长久登录状态
			LmMemberToken mt = new LmMemberToken();
			mt.setTokenid(tokenId);
			mt.setPassId(u.getUserid());
			mt.setUserAgent(userAgent);
			mt.setDevType(devType);
			mt.setLoginTime(new Date());
			mt.setState("1");
			lmMemberTokenService.save(mt);
		}
//		System.out.println("tokenid="+tokenId);
		vo.setTokenId(tokenId);
		LoginResp resp = new LoginResp();
		resp.setUser(vo);
		return resp;
	}
	
	@ApiOperation(value = "获取用户当前登录会话信息", nickname="fetchCurrentUser")
	@Security(session=true)
	@RequestMapping(value="/fetchCurrentUser",method=RequestMethod.GET)
	public LoginResp fetchCurrentUser() {
		BaseUserInfo user = (BaseUserInfo)SsoContext.getUser();
		user.setTokenId(SsoContext.getToken());
		LoginResp resp = new LoginResp();
		resp.setUser(user);
		return resp;
	}

	@Cacheable(cacheNames="quickcache")
	@Security(session=false)
	@RequestMapping(value="/testCache",method=RequestMethod.GET)
	public ApiResponse testCache(@RequestParam("id")String id) {
		ApiResponse resp = new BasicApiResponse();
		resp.setMessage(System.currentTimeMillis()+"");
		return resp;
	}
	
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			SysUserDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<SysUser> result = sysUserService.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
			myBuilder.addProperty("account", "账号");
			myBuilder.addProperty("fullname", "用户姓名");
			myBuilder.addProperty("createtime", "创建时间", FieldType.BASE_DATE, "yyyy-MM-dd");//设置时间格式
			myBuilder.addProperty("statusName", "用户状态");
			
			myBuilder.buildSheet("用户列表", result.getContent());//放到第一个sheet
			
			String filename = "用户列表("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
			response.setContentType(ContentType.EXCEL);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			//开始导出
			myBuilder.finish();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}
	
}
