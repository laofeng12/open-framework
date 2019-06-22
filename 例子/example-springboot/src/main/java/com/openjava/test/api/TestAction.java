package com.openjava.test.api;

import org.ljdp.component.exception.APIException;
import org.ljdp.secure.annotation.Security;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.openjava.nhc.test.domain.ExampleOrder;

import io.swagger.annotations.Api;

@Api(tags="测试管理")
@RestController
@RequestMapping("/test")
public class TestAction {

	@Security(session=false)
	@RequestMapping(value="/test1",method=RequestMethod.GET)
	public ResponseEntity<ExampleOrder> test1() throws Exception{
		if(true) {
			throw new APIException(30001, "测试出现异常");
		}
		return ResponseEntity.ok(new ExampleOrder());
	}
	
	@Security(session=false)
	@RequestMapping(value="/test2",method=RequestMethod.GET)
	public ResponseEntity<ExampleOrder> test2() throws Exception{
		if(true) {
			throw new APIException(30002, "测试出现异常");
		}
		return ResponseEntity.ok(new ExampleOrder());
	}
	
	@Security(session=true)
	@RequestMapping(value="/test3",method=RequestMethod.GET)
	public ResponseEntity<ExampleOrder> test3() throws Exception{
		System.out.println("==========");
		return ResponseEntity.ok(new ExampleOrder());
	}
	
	@Security(session=false)
	@RequestMapping(value="/test4",method=RequestMethod.GET)
	public void test4() throws Exception{
		if(true) {
			throw new APIException(30003, "测试出现异常");
		}
	}
	
	@Security(session=false)
	@RequestMapping(value="/test1b",method=RequestMethod.GET)
	public ExampleOrder test1b() throws Exception{
		if(true) {
			throw new APIException(30001, "测试出现异常");
		}
		return new ExampleOrder();
	}
	
	@Security(session=false)
	@RequestMapping(value="/test2b",method=RequestMethod.GET)
	public ExampleOrder test2b() throws Exception{
		if(true) {
			throw new APIException(30002, "测试出现异常");
		}
		return new ExampleOrder();
	}
	
	@Security(session=true)
	@RequestMapping(value="/test3b",method=RequestMethod.GET)
	public ExampleOrder test3b() throws Exception{
		System.out.println("==========");
		return new ExampleOrder();
	}
	
	
}
