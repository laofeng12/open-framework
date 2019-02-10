package com.openjava.example.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.openjava.example.order.domain.ExampleOrder;

@Mapper
public interface OrderMapper {

	List<ExampleOrder> selectAll();
	
	List<ExampleOrder> selectAll2();
}
