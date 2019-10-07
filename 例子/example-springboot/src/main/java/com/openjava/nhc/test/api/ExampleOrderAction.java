package com.openjava.nhc.test.api;

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
import org.ljdp.component.result.SuccessMessage;
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

import com.openjava.nhc.test.domain.ExampleOrder;
import com.openjava.nhc.test.service.ExampleOrderService;
import com.openjava.nhc.test.query.ExampleOrderDBParam;


/**
 * api接口
 * @author 自由
 *
 */
@Api(tags="订单管理")
@RestController
@RequestMapping("/nhc/test/exampleOrder")
public class ExampleOrderAction {
	
	@Resource
	private ExampleOrderService exampleOrderService;
	
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
	@Security(session=false)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ExampleOrder get(@PathVariable("id")String id) {
		ExampleOrder m = exampleOrderService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_operAccount", value = "下单账号like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_submitTime", value = "下单时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_submitTime", value = "下单时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "eq_totalPrice", value = "订单总额=", required = false, dataType = "Float", paramType = "query"),
		@ApiImplicitParam(name = "like_userName", value = "用户名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_orderStatus", value = "订单状态=", required = false, dataType = "Float", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=false)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<ExampleOrder> doSearch(@ApiIgnore() ExampleOrderDBParam params, @ApiIgnore() Pageable pageable){
		Page<ExampleOrder> result =  exampleOrderService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	@Security(session=false)
	@RequestMapping(value="/searchslave",method=RequestMethod.GET)
	public TablePage<ExampleOrder> doSearchSlave(@ApiIgnore() ExampleOrderDBParam params, @ApiIgnore() Pageable pageable){
		Page<ExampleOrder> result =  exampleOrderService.querySlave(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	@Security(session=false)
	@RequestMapping(value="/search2",method=RequestMethod.GET)
	public TablePage<ExampleOrder> doSearch2(@ApiIgnore() ExampleOrderDBParam params, @ApiIgnore() Pageable pageable){
		Page<ExampleOrder> result =  exampleOrderService.query2(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	@Security(session=false)
	@RequestMapping(value="/search2slave",method=RequestMethod.GET)
	public TablePage<ExampleOrder> doSearch2Slave(@ApiIgnore() ExampleOrderDBParam params, @ApiIgnore() Pageable pageable){
		Page<ExampleOrder> result =  exampleOrderService.query2Slave(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=false)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody ExampleOrder body) {
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setOrderId(ss.getSequence(""));
		body.setIsNew(true);//执行insert
		ExampleOrder dbObj = exampleOrderService.doSave(body);
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}
	
	/**
	 * 更新
	 */
	@ApiOperation(value = "更新", nickname="update", notes = "报文格式：content-type=application/json")
	@Security(session=false)
	@RequestMapping(method=RequestMethod.PATCH)
	public SuccessMessage doUpdate(@RequestBody ExampleOrder body) {
		//修改，记录更新时间等
		ExampleOrder db = exampleOrderService.get(body.getOrderId());
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		exampleOrderService.doSave(db);
		return new SuccessMessage("保存成功");
	}
	
	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=false)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)String id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			exampleOrderService.doDelete(id);
		} else if(ids != null) {
			exampleOrderService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			ExampleOrderDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<ExampleOrder> result = exampleOrderService.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
//			myBuilder.addProperty("useraccount", "账号");
//			myBuilder.addProperty("username", "用户姓名");
//			myBuilder.addProperty("creatime", "创建时间", FieldType.BASE_DATE, "yyyy-MM-dd");//设置时间格式
//			myBuilder.addProperty("userStatus", "用户状态", SysCodeUtil.codeToMap("sys.user.status"));//自动数据字典【tsys_code】翻译
//			Map<K, V> tfMap1 = new HashMap();
//			tfMap1.put(1, "状态1");
//			tfMap1.put(2, "状态2");
//			myBuilder.addProperty("userStatus", "用户状态",tfMap1);//写死静态字典翻译
			
			myBuilder.buildSheet("", result.getContent());//放到第一个sheet
			
			String filename = "("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
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
}
