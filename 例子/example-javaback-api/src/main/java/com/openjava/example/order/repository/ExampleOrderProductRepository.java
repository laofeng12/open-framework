package com.openjava.example.order.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.example.order.domain.ExampleOrderProduct;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface ExampleOrderProductRepository extends DynamicJpaRepository<ExampleOrderProduct, String>, ExampleOrderProductRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete ExampleOrderProduct t where t.orderItemId=:orderItemId")
	public int deleteByPkId(@Param("orderItemId")String orderItemId);
}
