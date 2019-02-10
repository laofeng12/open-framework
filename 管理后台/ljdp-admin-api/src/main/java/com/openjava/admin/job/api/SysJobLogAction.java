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

import com.openjava.admin.job.domain.SysJobLog;
import com.openjava.admin.job.service.SysJobLogService;
import com.openjava.admin.job.query.SysJobLogDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="任务执行日志")
@RestController
@RequestMapping("/admin/job/sysJobLog")
public class SysJobLogAction {
	
	@Resource
	private SysJobLogService sysJobLogService;
	
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
	public SysJobLog get(@PathVariable("id")Long id) {
		SysJobLog m = sysJobLogService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_jobId", value = "任务编号=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_status", value = "状态(schedule.job.status) 0：待执行 1：运行中 2：成功 3：失败=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "le_startTime", value = "开始时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_startTime", value = "开始时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysJobLog> doSearch(@ApiIgnore() SysJobLogDBParam params, @ApiIgnore() Pageable pageable){
		Page<SysJobLog> result =  sysJobLogService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "logSeq", value = "日志ID", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "jobId", value = "任务编号", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "jobParams", value = "参数", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "finishTime", value = "完成时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "status", value = "状态(schedule.job.status) 0：待执行 1：运行中 2：成功 3：失败", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "errorLog", value = "错误日志", required = false, dataType = "String", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(@ApiIgnore() SysJobLog model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setLogSeq(ss.getSequence());
			SysJobLog dbObj = sysJobLogService.doSave(model);
		} else {
			//修改，记录更新时间等
			SysJobLog db = sysJobLogService.get(model.getLogSeq());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			sysJobLogService.doSave(db);
		}
		
		BasicApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	
}
