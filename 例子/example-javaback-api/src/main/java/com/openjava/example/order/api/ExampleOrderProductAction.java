package com.openjava.example.order.api;

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

import com.openjava.example.order.domain.ExampleOrderProduct;
import com.openjava.example.order.service.ExampleOrderProductService;
import com.openjava.example.order.query.ExampleOrderProductDBParam;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="订单明细")
@RestController
@RequestMapping("/example/order/exampleOrderProduct")
public class ExampleOrderProductAction {
	
	@Resource
	private ExampleOrderProductService exampleOrderProductService;
	
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
	public ExampleOrderProduct get(@PathVariable("id")String id) {
		ExampleOrderProduct m = exampleOrderProductService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_orderId", value = "订单ID=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "like_shopName", value = "商家名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "like_productName", value = "产品名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_salePrice", value = "销售价格<=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "ge_salePrice", value = "销售价格>=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<ExampleOrderProduct> doSearch(ExampleOrderProductDBParam params, Pageable pageable){
		Page<ExampleOrderProduct> result =  exampleOrderProductService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "orderItemId", value = "订单明细ID", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "orderId", value = "订单ID", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "shopName", value = "商家名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "productName", value = "产品名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "productSpecVal1", value = "产品规格1值", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "productSpecVal2", value = "产品规格2值", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "salePrice", value = "销售价格", required = false, dataType = "Long", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(ExampleOrderProduct model, @RequestParam("isNew")Boolean isNew

			) {
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setOrderItemId(ss.getSequence(""));
			ExampleOrderProduct dbObj = exampleOrderProductService.doSave(model);
		} else {
			//修改，记录更新时间等
			ExampleOrderProduct db = exampleOrderProductService.get(model.getOrderItemId());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			exampleOrderProductService.doSave(db);
		}
		
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")String id) {
		exampleOrderProductService.doDelete(id);
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
		exampleOrderProductService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
}
