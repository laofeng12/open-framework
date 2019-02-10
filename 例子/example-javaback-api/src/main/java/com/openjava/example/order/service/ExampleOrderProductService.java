package com.openjava.example.order.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.example.order.domain.ExampleOrderProduct;
import com.openjava.example.order.query.ExampleOrderProductDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface ExampleOrderProductService {
	public Page<ExampleOrderProduct> query(ExampleOrderProductDBParam params, Pageable pageable);
	
	public List<ExampleOrderProduct> queryDataOnly(ExampleOrderProductDBParam params, Pageable pageable);
	
	public ExampleOrderProduct get(String id);
	
	public ExampleOrderProduct doSave(ExampleOrderProduct m);
	
	public void doDelete(String id);
	public void doRemove(String ids);
}
