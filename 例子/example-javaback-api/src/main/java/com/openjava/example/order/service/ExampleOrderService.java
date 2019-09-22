package com.openjava.example.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.vo.OrderDetailView;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface ExampleOrderService {
	public Page<ExampleOrder> query(ExampleOrderDBParam params, Pageable pageable);
	
	public List<ExampleOrder> queryDataOnly(ExampleOrderDBParam params, Pageable pageable);
	
	public ExampleOrder get(Long id);
	
	public ExampleOrder doSave(ExampleOrder m);
	
	public void doDelete(Long id);
	public void doRemove(String ids);
	
	/**
	 * 多表联表查询例子1：动态参数
	 * 获取订单与订单购买的商品明细记录
	 */
	public Page<OrderDetailView> queryOrderProducts(
			ExampleOrderDBParam orderParams,
			ExampleOrderProductDBParam prodParams,
			Pageable pageable);
	
	/**
	 * 多表联表查询例子2：固定参数
	 */
	public Page<OrderDetailView> queryOrdersByUserAndShop(
			String operAccount, Date submitTime, String shopName,
			Pageable pageable);
	
	public List<ExampleOrder> queryAll() throws Exception;
	List<ExampleOrder> selectAll2();
}
