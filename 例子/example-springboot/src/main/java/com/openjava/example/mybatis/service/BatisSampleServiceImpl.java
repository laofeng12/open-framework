package com.openjava.example.mybatis.service;

import java.util.List;

import javax.annotation.Resource;

import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.example.mybatis.dao.CityDao;
import com.openjava.example.mybatis.domain.City;
import com.openjava.example.mybatis.domain.Hotel;
import com.openjava.example.mybatis.mapper.CityMapper;
import com.openjava.example.mybatis.mapper.HotelMapper;

@Service
@Transactional
public class BatisSampleServiceImpl implements BatisSampleService {

	@Resource
	private HotelMapper hotelMapper;
	@Resource
	private CityMapper cityMapper;
	@Resource
	private CityDao cityDao;
	
	@Override
	public Hotel selectByCityId(int city_id) {
		return hotelMapper.selectByCityId(city_id);
	}
	
	@Override
	public City selectCityById(long id) {
		return cityMapper.selectCityById(id);
	}
	
	@Override
	public City selectCityByIdDao(long id) {
		return cityDao.selectCityById(id);
	}
	
	@Override
	public List<Hotel> selectAll(){
		return hotelMapper.selectAll();
	}
	
	@Override
	public List<City> findByState(String state) {
		return cityMapper.findByState(state);
	}
	
	@Override
	public void doSaveCity(String name) throws Exception{
		City city = new City();
		city.setId(ConcurrentSequence.getInstance().getSequence());
		city.setName(name);
		city.setCountry("中国");
		city.setState("广东");
		cityMapper.insertCity(city);
	}
}
