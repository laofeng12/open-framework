package com.openjava.example.mybatis.service;

import java.util.List;

import com.openjava.example.mybatis.domain.City;
import com.openjava.example.mybatis.domain.Hotel;

public interface BatisSampleService {
	Hotel selectByCityId(int city_id);
	
	City selectCityById(long id);
	
	City selectCityByIdDao(long id);
	
	List<Hotel> selectAll();
	
	City findByState(String state);
	
	void doSaveCityAndOrder() throws Exception;
}
