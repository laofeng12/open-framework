package com.openjava.example.mybatis.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.TimeSequence;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.example.mybatis.dao.CityDao;
import com.openjava.example.mybatis.domain.City;
import com.openjava.example.mybatis.domain.Hotel;
import com.openjava.example.mybatis.mapper.CityMapper;
import com.openjava.example.mybatis.mapper.HotelMapper;
import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.repository.ExampleOrderRepository;

@Service
@Transactional
public class BatisSampleServiceImpl implements BatisSampleService {

	@Resource
	private HotelMapper hotelMapper;
	@Resource
	private CityMapper cityMapper;
	@Resource
	private CityDao cityDao;
	@Resource
	private ExampleOrderRepository exampleOrderRepository;
	
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
	public City findByState(String state) {
		return cityMapper.findByState(state);
	}
	
	@Override
	public void doSaveCityAndOrder() throws Exception{
		City city = new City();
		city.setId(ConcurrentSequence.getInstance().getSequence());
		city.setName("东莞");
		city.setCountry("中国");
		city.setState("市");
		cityMapper.insertCity(city);
		
		ExampleOrder order = new ExampleOrder();
		order.setOrderId(TimeSequence.getInstance().getSequence());
		order.setOperAccount("mybatis");
		order.setOrderStatus(9L);
		order.setSubmitTime(new Date());
		order.setTotalPrice(9875L);
		order.setUserName("测试");
		order.setUserAddress("华凯广场B卓1111");
		exampleOrderRepository.save(order);
		if(true) {
			throw new APIException("测试回滚");
		}
	}
}
