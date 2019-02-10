package com.openjava.admin.job.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
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
import springfox.documentation.annotations.ApiIgnore;

import com.openjava.admin.job.domain.SysJobWaitQueue;
import com.openjava.admin.job.service.SysJobWaitQueueService;
import com.openjava.admin.job.query.SysJobWaitQueueDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="待执行任务队列")
@RestController
@RequestMapping("/admin/job/sysJobWaitQueue")
public class SysJobWaitQueueAction {
	
	@Resource
	private SysJobWaitQueueService sysJobWaitQueueService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysJobWaitQueue get(@PathVariable("id")Long id) {
		SysJobWaitQueue m = sysJobWaitQueueService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_jobId", value = "任务编号=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_scheduleTime", value = "计划执行时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_scheduleTime", value = "计划执行时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysJobWaitQueue> doSearch(@ApiIgnore() SysJobWaitQueueDBParam params, @ApiIgnore() Pageable pageable){
		Page<SysJobWaitQueue> result =  sysJobWaitQueueService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queueNo", value = "队列序号", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "jobId", value = "任务编号", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "jobParams", value = "参数", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "scheduleTime", value = "计划执行时间", required = false, dataType = "Date", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(@ApiIgnore() SysJobWaitQueue model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setQueueNo(ss.getSequence());
			SysJobWaitQueue dbObj = sysJobWaitQueueService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysJobWaitQueue db = sysJobWaitQueueService.get(model.getQueueNo());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			sysJobWaitQueueService.doSave(db);
		}
		
		BasicApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		sysJobWaitQueueService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "批量删除", nickname="remove")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public ApiResponse doRemove(@RequestParam("ids")String ids) {
		sysJobWaitQueueService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
}
