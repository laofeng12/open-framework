package com.openjava.example.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.vo.OrderDetailView;

public interface ExampleOrderRepositoryCustom {

	public Page<OrderDetailView> queryOrderProducts(
			ExampleOrderDBParam orderParams,
			ExampleOrderProductDBParam prodParams,
			Pageable pageable);
}
