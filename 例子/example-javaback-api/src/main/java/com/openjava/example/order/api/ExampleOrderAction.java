package com.openjava.example.order.api;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
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

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.service.ExampleOrderService;
import com.openjava.example.order.vo.ExampleOrderRequest;
import com.openjava.example.order.vo.OrderDetailView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;


/**
 * api接口
 * @author hzy
 *
 */
@Api(tags="订单管理")
@RestController
@RequestMapping("/example/order/exampleOrder")
public class ExampleOrderAction {
	
	@Resource
	private ExampleOrderService exampleOrderService;

	/**
	 * 多表关联查询例子1：动态参数
	 * @param orderParams 表1参数
	 * @param prodParams  表2参数
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value="/searchDetail",method=RequestMethod.GET)
	public TablePage<OrderDetailView> doSearchDetail(
			ExampleOrderDBParam orderParams,
			ExampleOrderProductDBParam prodParams,
			Pageable pageable){
		Page<OrderDetailView> result =  exampleOrderService.queryOrderProducts(orderParams, prodParams, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	/**
	 * 多表关联查询例子2：固定参数
	 * @param operAccount 用户
	 * @param submitTime 查询时间
	 * @param shopName 商家名称
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value="/searchUserOrders",method=RequestMethod.GET)
	public TablePage<OrderDetailView> doSearchUserOrders(
			String operAccount, Date submitTime, String shopName,
			Pageable pageable){
		Page<OrderDetailView> result =  exampleOrderService.queryOrdersByUserAndShop(
				operAccount, submitTime, shopName, pageable);
		
		return new TablePageImpl<>(result);
	}
	
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
	@Security(session=false)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ExampleOrder get(@PathVariable("id")Long id) throws Exception{
//		System.out.println("SSO-user");
//		System.out.println(ToStringBuilder.reflectionToString(SsoContext.getUser(), ToStringStyle.MULTI_LINE_STYLE));
		ExampleOrder m = exampleOrderService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_operAccount", value = "下单用户=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_submitTime", value = "下单时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_submitTime", value = "下单时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "eq_orderStatus", value = "订单状态=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=false)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<ExampleOrder> doSearch(@ApiIgnore() ExampleOrderDBParam params, @ApiIgnore() Pageable pageable){
		
//		params.setEq_totalPrice(new Long[] {100L, 288L, 399L});
//		List<String> in_userName = new ArrayList<String>();
//		in_userName.add("张三");
//		in_userName.add("李四");
//		params.setIn_userName(in_userName);
//		
//		params.setIn_userAddress("'a','b','c','d','e'");
//		
//		params.setTableAlias("t");
//		params.setSql_query1("exists("
//				+ "select 1 from ExampleOrderProduct p where p.orderId=t.orderId"
//				+ " and p.shopName='六沐商城')");

		Page<ExampleOrder> result =  exampleOrderService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "orderId", value = "订单号", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "operAccount", value = "下单用户", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "submitTime", value = "下单时间", required = false, dataType = "Date", paramType = "post"),
		@ApiImplicitParam(name = "totalPrice", value = "订单总额", required = false, dataType = "Long", paramType = "post"),
		@ApiImplicitParam(name = "userName", value = "收货人名称", required = false, dataType = "String", paramType = "post"),
		@ApiImplicitParam(name = "userAddress", value = "收货地址", required = false, dataType = "String", paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public ApiResponse doSave(ExampleOrder model, @RequestParam("isNew")Boolean isNew) throws Exception{
		if(isNew) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			model.setOrderId(ss.getSequence());
			ExampleOrder dbObj = exampleOrderService.doSave(model);
		} else {
			//修改，记录更新时间等
			ExampleOrder db = exampleOrderService.get(model.getOrderId());
			MyBeanUtils.copyPropertiesNotBlank(db, model);
			exampleOrderService.doSave(db);
		}
		
		DataApiResponse resp = new DataApiResponse();
		return resp;
	}
	
	@ApiOperation(value = "json报文格式提交保存")
	@Security(session = false)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ApiResponse doSave2(@RequestBody ExampleOrderRequest req) throws Exception{
		ExampleOrder model = new ExampleOrder();
		BeanUtils.copyProperties(model, req);
		return doSave(model, req.getIsNew());
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = true, paramType = "post"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public ApiResponse doDelete(@RequestParam("id")Long id) {
		exampleOrderService.doDelete(id);
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
		exampleOrderService.doRemove(ids);
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export",method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			ExampleOrderDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<ExampleOrder> result = exampleOrderService.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
			myBuilder.addProperty("orderId", "订单号");
			myBuilder.addProperty("operAccount", "下单用户");
			myBuilder.addProperty("submitTime", "下单时间");
			myBuilder.addProperty("totalPrice", "订单总额");
			
			myBuilder.buildSheet("例子-订单管理", result.getContent());//放到第一个sheet
			
			String filename = "例子-订单管理("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
			response.setContentType(ContentType.EXCEL);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			//开始导出
			myBuilder.finish();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}
	
	@ApiOperation(value = "测试api文档显示", consumes="消费",nickname="testswagger",notes="备注说明",produces="提供者")
	@RequestMapping(value="/testswagger",method=RequestMethod.GET)
	public ApiResponse testswagger() {
		ApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	@RequestMapping(value="/selectAll",method=RequestMethod.GET)
	public DataApiResponse<ExampleOrder> selectAll() throws Exception{
		List<ExampleOrder> list = exampleOrderService.queryAll();
		DataApiResponse<ExampleOrder> resp = new DataApiResponse<>();
		resp.setRows(list);
		return resp;
	}
	@RequestMapping(value="/selectAll2",method=RequestMethod.GET)
	public DataApiResponse<ExampleOrder> selectAll2() {
		List<ExampleOrder> list = exampleOrderService.selectAll2();
		DataApiResponse<ExampleOrder> resp = new DataApiResponse<>();
		resp.setRows(list);
		return resp;
	}
}
