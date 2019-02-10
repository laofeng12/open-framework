package com.openjava.example.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.openjava.example.mybatis.domain.City;

@Mapper
public interface CityMapper {

	City selectCityById(long id);
	
	@Select("select * from city where state = #{state}")
	City findByState(@Param("state") String state);
	
	void insertCity(City city);
}
