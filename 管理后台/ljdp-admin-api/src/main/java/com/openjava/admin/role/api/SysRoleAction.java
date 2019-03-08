package com.openjava.admin.role.api;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.role.query.SysRoleDBParam;
import com.openjava.admin.role.service.SysRoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="角色管理")
@RestController
@RequestMapping("/admin/role/sysRole")
public class SysRoleAction {
	
	@Resource
	private SysRoleService sysRoleService;
	
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
	public SysRole get(@PathVariable("id")Long id) {
		SysRole m = sysRoleService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_rolename", value = "角色名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_allowdel", value = "是否允许删除=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_allowedit", value = "是否允许编辑=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_enabled", value = "是否启用=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysRole> doSearch(SysRoleDBParam params, Pageable pageable){
		if(StringUtils.isNotBlank(params.getLike_rolename())) {
			params.setLike_rolename("%"+params.getLike_rolename()+"%");
		}
		if(StringUtils.isNotBlank(params.getLike_alias())) {
			params.setLike_alias("%"+params.getLike_alias()+"%");
		}
		Page<SysRole> result =  sysRoleService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleid", value = "角色id", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "systemid", value = "系统编码", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "alias", value = "别名", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "rolename", value = "角色名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "memo", value = "备注", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "allowdel", value = "是否允许删除", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "allowedit", value = "是否允许编辑", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "enabled", value = "是否启用", required = false, dataType = "Short", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysRole model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setRoleid(ss.getSequence());
		} else {
			//修改，记录更新时间等
		}
		SysRole dbObj = sysRoleService.doSave(model);
		ApiResponse resp = new BasicApiResponse(200);
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysRoleService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	/**
	 * 更新角色菜单权限信息
	 * @param roleid
	 * @param resIds
	 * @return
	 */
	@ApiOperation(value = "更新角色菜单权限信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleid", value = "需要更新的角色id", required = true, paramType = "post"),
		@ApiImplicitParam(name = "resIds", value = "角色最新的资源权限id，多个id直接用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/updateRes",method=RequestMethod.POST)
	public ApiResponse updateRes(@RequestParam("roleid")Long roleid, @RequestParam("resIds")String resIds) {
		sysRoleService.updateRoleRes(roleid, resIds);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
}
