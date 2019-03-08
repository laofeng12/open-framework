package com.openjava.admin.sys.api;

import java.util.Date;

import javax.annotation.Resource;

import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.sys.domain.SysNotice;
import com.openjava.admin.sys.query.SysNoticeDBParam;
import com.openjava.admin.sys.service.SysNoticeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;


/**
 * api接口
 * @author heizyou
 *
 */
@Api(tags="通知管理")
@RestController
@RequestMapping("/admin/sys/sysNotice")
public class SysNoticeAction {
	
	@Resource
	private SysNoticeService sysNoticeService;
	
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
	public SysNotice get(@PathVariable("id")String id) {
		SysNotice m = sysNoticeService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_ntype", value = "通知类型（notice.type）=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "like_title", value = "通知标题like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_createTime", value = "创建时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_createTime", value = "创建时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "le_updateTime", value = "更新时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_updateTime", value = "更新时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "eq_nstatus", value = "通知状态（notice.status）=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysNotice> doSearch(SysNoticeDBParam params, Pageable pageable){
		Page<SysNotice> result =  sysNoticeService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "nid", value = "通知id", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "ntype", value = "通知类型（notice.type）", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "title", value = "通知标题", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "content", value = "通知内容", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "picBanner", value = "通知banner图", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "createUser", value = "创建人", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "createTime", value = "创建时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "updateUser", value = "更新人员", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "updateTime", value = "更新时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "nstatus", value = "通知状态（notice.status）", required = false, dataType = "String", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(SysNotice model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setNid(ss.getSequence(""));
			model.setCreateTime(new Date());
			model.setCreateUser(SsoContext.getAccount());
			model.setUpdateTime(new Date());
			model.setUpdateUser(SsoContext.getAccount());
			model.setNstatus("1");
			SysNotice dbObj = sysNoticeService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysNotice db = sysNoticeService.get(model.getNid());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			db.setUpdateTime(new Date());
			db.setUpdateUser(SsoContext.getAccount());
			db.setNstatus("1");
			sysNoticeService.doSave(db);
		}
		
		ApiResponse resp = new BasicApiResponse(200);
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")String id) {
		sysNoticeService.doDelete(id);
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
		sysNoticeService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "批量发布通知")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/publish",method=RequestMethod.POST)
	public ApiResponse doPublish(@RequestParam("ids")String ids) {
		int c = sysNoticeService.doPublish(ids);
		ApiResponse resp = new BasicApiResponse();
		resp.setMessage("成功发布"+c+"条");
		return resp;
	}
	
	@ApiOperation(value = "批量下架通知")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/downNotic",method=RequestMethod.POST)
	public ApiResponse doDownNotic(@RequestParam("ids")String ids) {
		int c = sysNoticeService.doDownNotic(ids);
		ApiResponse resp = new BasicApiResponse();
		resp.setMessage("成功下架"+c+"条");
		return resp;
	}
	
	@ApiOperation(value = "审核通过")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/pass",method=RequestMethod.POST)
	public ApiResponse auditPass(@RequestParam("ids")String ids) {
		int c = sysNoticeService.auditPass(ids);
		ApiResponse resp = new BasicApiResponse();
		resp.setMessage("成功"+c+"条");
		return resp;
	}
	
	@ApiOperation(value = "审核不通过")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/notpass",method=RequestMethod.POST)
	public ApiResponse auditNotPass(@RequestParam("ids")String ids) {
		int c = sysNoticeService.auditNotPass(ids);
		ApiResponse resp = new BasicApiResponse();
		resp.setMessage("成功"+c+"条");
		return resp;
	}
}
