package com.openjava.admin.logs.api;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.log.annotation.LogConfig;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.admin.logs.domain.SysLogApiError;
import com.openjava.admin.logs.domain.SysLogApiRequest;
import com.openjava.admin.logs.query.LogApiErrorDBParam;
import com.openjava.admin.logs.service.LogApiErrorService;
import com.openjava.admin.logs.service.LogApiRequestService;
import com.openjava.admin.logs.vo.SysApiLogVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;


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
	@LogConfig(save=false)
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public DataApiResponse<SysApiLogVO> get(@PathVariable("id")String id) {
		SysLogApiError m = logApiErrorService.get(id);
		SysApiLogVO vo = new SysApiLogVO();
		if(m != null) {
			SysLogApiRequest req = logApiRequestService.get(new Long(m.getRequestId()));
			try {
				PropertyUtils.copyProperties(vo, m);
				if(req != null) {
					PropertyUtils.copyProperties(vo, req);
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setError(e.getMessage());
			}
		} else {
			vo.setError("日志不存在");
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
	@LogConfig(save=false)
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<SysLogApiError> doSearch(@ApiIgnore() LogApiErrorDBParam params, @ApiIgnore() Pageable pageable){
		Page<SysLogApiError> result =  logApiErrorService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	
	
}
