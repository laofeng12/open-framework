package com.openjava.admin.logs.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
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

import com.openjava.admin.logs.domain.SysLogApiError;
import com.openjava.admin.logs.domain.SysLogApiRequest;
import com.openjava.admin.logs.service.LogApiErrorService;
import com.openjava.admin.logs.service.LogApiRequestService;
import com.openjava.admin.logs.vo.SysApiLogVO;
import com.openjava.admin.logs.query.LogApiErrorDBParam;


/**
 * api接口
 * @author heziyou
 *
 */
@Api(tags="错误日志查询")
@RestController
@RequestMapping("/admin/logs/logApiError")
public class LogApiErrorAction {
	
	@Resource
	private LogApiErrorService logApiErrorService;
	@Resource
	private LogApiRequestService logApiRequestService;
	
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
	public DataApiResponse<SysApiLogVO> get(@PathVariable("id")String id) {
		SysLogApiError m = logApiErrorService.get(id);
		SysLogApiRequest req = logApiRequestService.get(new Long(m.getRequestId()));
		SysApiLogVO vo = new SysApiLogVO();
		try {			
			PropertyUtils.copyProperties(vo, req);
			PropertyUtils.copyProperties(vo, m);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setError(e.getMessage());
		}
		DataApiResponse<SysApiLogVO> apiResp = new DataApiResponse<>();
		apiResp.setData(vo);
		return apiResp;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_requestId", value = "接口请求ID=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_errorDate", value = "错误时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_errorDate", value = "错误时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysLogApiError> doSearch(@ApiIgnore() LogApiErrorDBParam params, @ApiIgnore() Pageable pageable){
		Page<SysLogApiError> result =  logApiErrorService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	
	
}
