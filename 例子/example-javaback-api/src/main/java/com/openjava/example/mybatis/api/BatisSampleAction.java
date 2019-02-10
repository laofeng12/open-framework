package com.openjava.example.mybatis.api;

import java.util.List;

import javax.annotation.Resource;

import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.example.mybatis.domain.City;
import com.openjava.example.mybatis.domain.Hotel;
import com.openjava.example.mybatis.service.BatisSampleService;

@RestController
@RequestMapping("/example/mybatis/sample")
public class BatisSampleAction {

	@Resource
	private BatisSampleService batisSampleService;
	
	@RequestMapping(value="/selectByCityId",method=RequestMethod.GET)
	public DataApiResponse<Hotel> selectByCityId(@RequestParam("id")Integer id) {
		Hotel hotel = batisSampleService.selectByCityId(id);
		DataApiResponse<Hotel> resp = new DataApiResponse<>();
		resp.setData(hotel);
		return resp;
	}
	
	@RequestMapping(value="/selectAllHolte",method=RequestMethod.GET)
	public DataApiResponse<Hotel> selectAllHolte() {
		List<Hotel> rows = batisSampleService.selectAll();
		DataApiResponse<Hotel> resp = new DataApiResponse<>();
		resp.setRows(rows);
		return resp;
	}
	
	@RequestMapping(value="/selectCityById",method=RequestMethod.GET)
	public DataApiResponse<City> selectCityById(@RequestParam("id")Long id) {
		City city = batisSampleService.selectCityById(id);
		DataApiResponse<City> resp = new DataApiResponse<>();
		resp.setData(city);
		return resp;
	}
	
	@RequestMapping(value="/selectCityByIdDao",method=RequestMethod.GET)
	public DataApiResponse<City> selectCityByIdDao(@RequestParam("id")Long id) {
		City city = batisSampleService.selectCityByIdDao(id);
		DataApiResponse<City> resp = new DataApiResponse<>();
		resp.setData(city);
		return resp;
	}
	
	@RequestMapping(value="/findByState",method=RequestMethod.GET)
	public DataApiResponse<City> findByState(@RequestParam("state")String state) {
		City city = batisSampleService.findByState(state);
		DataApiResponse<City> resp = new DataApiResponse<>();
		resp.setData(city);
		return resp;
	}
	
	@RequestMapping(value="/testTransaction",method=RequestMethod.GET)
	public ApiResponse testTransaction() throws Exception{
		batisSampleService.doSaveCityAndOrder();
		ApiResponse resp = new BasicApiResponse(200);
		return resp;
	}
}
