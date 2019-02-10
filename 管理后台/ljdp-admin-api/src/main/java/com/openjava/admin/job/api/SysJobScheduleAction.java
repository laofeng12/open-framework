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

import com.openjava.admin.job.domain.SysJobSchedule;
import com.openjava.admin.job.service.SysJobScheduleService;
import com.openjava.admin.job.query.SysJobScheduleDBParam;


/**
 * api接口
 * @author 子右
 *
 */
@Api(tags="计划任务")
@RestController
@RequestMapping("/admin/job/sysJobSchedule")
public class SysJobScheduleAction {
	
	@Resource
	private SysJobScheduleService sysJobScheduleService;
	
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
	public SysJobSchedule get(@PathVariable("id")String id) {
		SysJobSchedule m = sysJobScheduleService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_jobName", value = "任务名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_jobClass", value = "类名=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_jobMethod", value = "方法=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysJobSchedule> doSearch(@ApiIgnore() SysJobScheduleDBParam params, @ApiIgnore() Pageable pageable){
		Page<SysJobSchedule> result =  sysJobScheduleService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "jobId", value = "任务编号", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "jobName", value = "任务名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "jobClass", value = "类名", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "jobMethod", value = "方法", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "lastStartTime", value = "最近一次运行时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "lastEndTime", value = "最近一次结束时间", required = false, dataType = "Date", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(@ApiIgnore() SysJobSchedule model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setJobId(ss.getSequence(""));
			SysJobSchedule dbObj = sysJobScheduleService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysJobSchedule db = sysJobScheduleService.get(model.getJobId());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			sysJobScheduleService.doSave(db);
		}
		
		BasicApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	/*@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")String id) {
		sysJobScheduleService.doDelete(id);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}*/
	
	/*@ApiOperation(value = "批量删除", nickname="remove")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "主键编码用,分隔", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public ApiResponse doRemove(@RequestParam("ids")String ids) {
		sysJobScheduleService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}*/
	
}
