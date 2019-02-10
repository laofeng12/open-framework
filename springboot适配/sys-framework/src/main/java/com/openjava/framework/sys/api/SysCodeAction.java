package com.openjava.framework.sys.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.ljdp.common.ehcache.MemoryCache;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.support.dictionary.DictConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import com.openjava.framework.sys.vo.DictCodeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="数据字典")
@RestController
@RequestMapping("/framework/sys/code")
public class SysCodeAction {
	
	@Resource
	private SysCodeService sysCodeService;
	@Value("${spring.profiles.active}")
	private String envActive;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public SysCode get(@PathVariable("id")Integer id) {
		SysCode m = sysCodeService.get(id);
		return m;
	}
	
	@ApiOperation(value = "查询字典(请改用v2)", notes = "根据类型查询下面的字典项目")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codetype", value = "字典类型", required = true, dataType = "string", paramType = "get"),
	})
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@Deprecated
	public DataApiResponse findByCodetype(@RequestParam("codetype")String codetype){
		List<DictCodeVO> list = fetchByCodetype(codetype);
		DataApiResponse apiresp = new DataApiResponse();
		apiresp.setData(list);
		return apiresp;
	}

	@ApiOperation(value = "查询字典v2", notes = "根据类型查询下面的字典项目")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codetype", value = "字典类型", required = true, dataType = "string", paramType = "get"),
	})
	@RequestMapping(value="/list2",method=RequestMethod.GET)
	public DataApiResponse<DictCodeVO> findByCodetype2(@RequestParam("codetype")String codetype){
		List<DictCodeVO> list = fetchByCodetype(codetype);
		DataApiResponse<DictCodeVO> apiresp = new DataApiResponse<>();
		apiresp.setRows(list);
		return apiresp;
	}

	private List<DictCodeVO> fetchByCodetype(String codetype) {
		List<DictCodeVO> list2 = (List<DictCodeVO>)MemoryCache.getData(DictConstants.CACHE_COMMON, codetype);
		if(null == list2) {
			List<SysCode> list = sysCodeService.findByCodetype(codetype);
			if(!list.isEmpty()) {
				final List<DictCodeVO> templist = new ArrayList<>(list.size());
				list.forEach(c -> {
					if(c.getStatus() == null) {
						templist.add(DictCodeVO.of(c));
					} else {
						if(c.getStatus().intValue() != 5) {
							templist.add(DictCodeVO.of(c));
						}
					}
				});
				list2 = templist;
				MemoryCache.putData(DictConstants.CACHE_COMMON, codetype, list2);
			}
		}
		return list2;
	}
	
	@ApiOperation(value = "查询字典(全部)", notes = "根据类型查询下面的字典项目")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codetype", value = "字典类型", required = true, dataType = "string", paramType = "get"),
	})
	@RequestMapping(value="/listAll",method=RequestMethod.GET)
	public DataApiResponse<DictCodeVO> findByCodetypAll(@RequestParam("codetype")String codetype){
		List<DictCodeVO> list = fetchByCodetypeAll(codetype);
		DataApiResponse<DictCodeVO> apiresp = new DataApiResponse<>();
		apiresp.setRows(list);
		return apiresp;
	}
	
	private List<DictCodeVO> fetchByCodetypeAll(String codetype) {
		List<DictCodeVO> list2 = (List<DictCodeVO>)MemoryCache.getData(DictConstants.CACHE_COMMON, "[ALL]"+codetype);
		if(null == list2) {
			List<SysCode> list = sysCodeService.findByCodetype(codetype);
			if(!list.isEmpty()) {
				final List<DictCodeVO> templist = new ArrayList<>(list.size());
				list.forEach(c -> {
					templist.add(DictCodeVO.of(c));
				});
				list2 = templist;
				MemoryCache.putData(DictConstants.CACHE_COMMON, "[ALL]"+codetype, list2);
			}
		}
		return list2;
	}
	
	
	@ApiOperation(value = "清理字典缓存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codetype", value = "字典类型", required = true, dataType = "string", paramType = "get"),
	})
	@RequestMapping(value="/clearCache",method=RequestMethod.GET)
	public ApiResponse clearCache(@RequestParam("codetype")String codetype){
		System.out.println("envActive="+envActive);
		MemoryCache.removeData(DictConstants.CACHE_COMMON, codetype);
		MemoryCache.removeData(DictConstants.CACHE_COMMON, "[ALL]"+codetype);
		ApiResponse res = new BasicApiResponse();
		return res;
	}
}
