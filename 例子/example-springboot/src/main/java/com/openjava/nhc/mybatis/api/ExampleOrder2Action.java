package com.openjava.nhc.mybatis.api;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.ljdp.component.result.SuccessMessage;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.IBatisPage;
import org.ljdp.ui.bootstrap.TablePage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.openjava.nhc.mybatis.domain.ExampleOrderEntity;
import com.openjava.nhc.mybatis.query.TestOrderDBParam;
import com.openjava.nhc.mybatis.service.IExampleOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="订单管理2")
@RestController
@RequestMapping("/nhc/mybatis/exampleOrder")
public class ExampleOrder2Action {

	@Resource
	private IExampleOrderService exampleOrderService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@Security(session=false)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ExampleOrderEntity get(@PathVariable("id")String id) {
		ExampleOrderEntity m = exampleOrderService.getById(id);
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
	public TablePage<ExampleOrderEntity> doSearch(@ApiIgnore() TestOrderDBParam params, @ApiIgnore() Pageable pageable){
		IPage<ExampleOrderEntity> result =  exampleOrderService.query(params, pageable);
		
		
		return new IBatisPage<>(result);
	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=false)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody ExampleOrderEntity body

			) {
		boolean res;
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
//			SequenceService ss = ConcurrentSequence.getInstance();
//			body.setOrderId(ss.getSequence(""));
			res = exampleOrderService.save(body);
		} else {
			//修改，记录更新时间等
			ExampleOrderEntity db = exampleOrderService.getById(body.getOrderId());
//			MyBeanUtils.copyPropertiesNotBlank(db, body);
			set(body.getOperAccount() != null, ()->db.setOperAccount(body.getOperAccount()));
			set(body.getSubmitTime() != null, ()->db.setSubmitTime(body.getSubmitTime()));
			set(body.getTotalPrice() != null, ()->db.setTotalPrice(body.getTotalPrice()));
			set(body.getUserName() != null, ()->db.setUserName(body.getUserName()));
			set(body.getUserAddress() != null, ()->db.setUserAddress(body.getUserAddress()));
			set(body.getOrderStatus() != null, ()->db.setOrderStatus(body.getOrderStatus()));
			res = exampleOrderService.updateById(db);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		if(res) {
			return new SuccessMessage("保存成功");
		}
		return new SuccessMessage("保存失败");
	}
	
	private void set(boolean condition, Runnable runnable) {
		if(condition) runnable.run();
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
		boolean res = false;
		if(id != null) {
			res = exampleOrderService.removeById(id);
		} else if(ids != null) {
			List<String> idList = Arrays.asList(ids.split(","));
			res = exampleOrderService.removeByIds(idList);
		}
		//没有需要返回的数据，就直接返回一条消息
		if(res) {
			return new SuccessMessage("删除成功");
		}
		return new SuccessMessage("删除失败");
	}
}
