package com.openjava.nhc.test.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.nhc.test.domain.ExampleOrder;
import com.openjava.nhc.test.query.ExampleOrderDBParam;

/**
 * 订单管理业务层接口
 * @author 自由
 *
 */
public interface ExampleOrderService {
	Page<ExampleOrder> query(ExampleOrderDBParam params, Pageable pageable);
	
	public Page<ExampleOrder> query2(ExampleOrderDBParam params, Pageable pageable);
	
	List<ExampleOrder> queryDataOnly(ExampleOrderDBParam params, Pageable pageable);
	
	ExampleOrder get(String id);
	
	ExampleOrder doSave(ExampleOrder m);
	
	void doDelete(String id);
	void doRemove(String ids);
}
