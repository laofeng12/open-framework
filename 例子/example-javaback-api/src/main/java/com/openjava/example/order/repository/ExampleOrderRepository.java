package com.openjava.example.order.repository;

import java.util.Date;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.vo.OrderDetailView;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface ExampleOrderRepository extends DynamicJpaRepository<ExampleOrder, Long>, ExampleOrderRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete ExampleOrder t where t.orderId=:orderId")
	public int deleteByPkId(@Param("orderId")Long orderId);
	
	/**
	 * 多表关联：查询用户最近在某个商家上面购买的订单
	 * @param Pageable
	 * @return
	 */
	@Query("select t1,t2 from ExampleOrder t1, ExampleOrderProduct t2 where t1.orderId=t2.orderId"
			+ " and t1.operAccount=:operAccount and t1.submitTime>=:submitTime"
			+ " and t2.shopName=:shopName")
	public Page<Object[]> queryOrdersByUserAndShop(
			@Param("operAccount")String operAccount,
			@Param("submitTime")Date submitTime,
			@Param("shopName")String shopName,
			Pageable pageable);
}
