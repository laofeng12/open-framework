package com.openjava.admin.user.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

import com.openjava.admin.user.domain.SysUserRole;
import com.openjava.admin.user.service.SysUserRoleService;
import com.openjava.admin.user.query.SysUserRoleDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="用户角色配置")
@RestController
@RequestMapping("/admin/user/sysUserRole")
public class SysUserRoleAction {
	
	@Resource
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysUserRole get(@PathVariable("id")Long id) {
		SysUserRole m = sysUserRoleService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_roleid", value = "角色ID=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_userid", value = "用户id=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysUserRole> doSearch(SysUserRoleDBParam params, Pageable pageable){
		Page<SysUserRole> result =  sysUserRoleService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "新增用户与角色关系", notes = "需要登录")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleid", value = "角色id", required = true, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "userid", value = "用户id", required = true, dataType = "Long", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysUserRole model) {
		SequenceService ss = ConcurrentSequence.getInstance();
		model.setUserroleid(ss.getSequence());
		SysUserRole dbObj = sysUserRoleService.doSave(model);
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysUserRoleService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
}
