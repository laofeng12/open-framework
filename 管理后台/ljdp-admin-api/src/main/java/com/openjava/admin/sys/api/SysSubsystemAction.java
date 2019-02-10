package com.openjava.admin.sys.api;

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
import org.ljdp.plugin.sys.resp.SystemResp;
import org.ljdp.plugin.sys.vo.SystemVO;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
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

import com.openjava.admin.sys.domain.SysSubsystem;
import com.openjava.admin.sys.service.SysSubsystemService;
import com.openjava.admin.sys.query.SysSubsystemDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="子系统管理")
@RestController
@RequestMapping("/admin/sys/sysSubsystem")
public class SysSubsystemAction {
	
	@Resource
	private SysSubsystemService sysSubsystemService;
	
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
	public SysSubsystem get(@PathVariable("id")Long id) {
		SysSubsystem m = sysSubsystemService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_sysname", value = "系统名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_allowdel", value = "是否允许删除=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_needorg", value = "是否需要组织=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_isactive", value = "是否激活=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "eq_islocal", value = "是否本地=", required = false, dataType = "Short", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysSubsystem> doSearch(SysSubsystemDBParam params, Pageable pageable){
		if(StringUtils.isNotBlank(params.getLike_sysname())) {
			params.setLike_sysname("%"+params.getLike_sysname()+"%");
		}
		Page<SysSubsystem> result =  sysSubsystemService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sysname", value = "系统名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "alias", value = "别名", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "logo", value = "系统LOGO", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "defaulturl", value = "默认地址", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "memo", value = "备注", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "allowdel", value = "是否允许删除", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "needorg", value = "是否需要组织", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "isactive", value = "是否激活", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "islocal", value = "是否本地", required = false, dataType = "Short", paramType = "post"),
		@ApiImplicitParam(name = "homepage", value = "主页", required = false, dataType = "String", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysSubsystem model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setSystemid(ss.getSequence());
			model.setCreatetime(new Date());
			model.setCreator(SsoContext.getAccount());
		} else {
			//修改，记录更新时间等
		}
		SysSubsystem dbObj = sysSubsystemService.doSave(model);
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
		sysSubsystemService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "获取当前登录账户的子系统列表",notes="需要登录后才能访问")
	@Security(session=true)
	@RequestMapping(value="/mySubsys",method=RequestMethod.GET)
	public SystemResp mySubsys() {
		Long userid = SsoContext.getUserId();
		List<SystemVO> systems = new ArrayList<>();
		List<SysSubsystem> syslist = sysSubsystemService.findMySubsystem(userid);
		syslist.forEach(sub -> {
			SystemVO v = new SystemVO();
			v.setSysId(sub.getSystemid().toString());
			v.setSysName(sub.getSysname());
			systems.add(v);
		});
		SystemResp resp = new SystemResp();
		resp.setSystems(systems);
		return resp;
	}
}
