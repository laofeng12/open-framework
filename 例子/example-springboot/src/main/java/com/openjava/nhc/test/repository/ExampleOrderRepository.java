package com.openjava.nhc.test.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.nhc.test.domain.ExampleOrder;

/**
 * 订单管理数据库访问层
 * @author 自由
 *
 */
public interface ExampleOrderRepository extends DynamicJpaRepository<ExampleOrder, String>, ExampleOrderRepositoryCustom{
	
}
