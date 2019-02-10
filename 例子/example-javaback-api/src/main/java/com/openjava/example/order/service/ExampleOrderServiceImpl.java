package com.openjava.example.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.domain.ExampleOrderProduct;
import com.openjava.example.order.mapper.OrderMapper;
import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.repository.ExampleOrderRepository;
import com.openjava.example.order.vo.OrderDetailView;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class ExampleOrderServiceImpl implements ExampleOrderService {
	
	@Resource
	private ExampleOrderRepository exampleOrderRepository;
	@Resource
	private SysCodeService sysCodeService;
	@Resource
	private OrderMapper orderMapper;
	
	@Override
	public Page<ExampleOrder> query(ExampleOrderDBParam params, Pageable pageable){
		Page<ExampleOrder> pageresult = exampleOrderRepository.query(params, pageable);
		Map<String, SysCode> orderstatus = sysCodeService.getCodeMap("order.status");
		for (ExampleOrder m : pageresult.getContent()) {
			if(m.getOrderStatus() != null) {
				SysCode c = orderstatus.get(m.getOrderStatus().toString());
				if(c != null) {
					m.setOrderStatusName(c.getCodename());
				}
			}
		}
		return pageresult;
	}

	/**
	 * 多表联表查询例子1：动态参数
	 * 获取订单与订单购买的商品明细记录
	 */
	@Override
	public Page<OrderDetailView> queryOrderProducts(ExampleOrderDBParam orderParams,
			ExampleOrderProductDBParam prodParams,
			Pageable pageable){
		return exampleOrderRepository.queryOrderProducts(orderParams, prodParams, pageable);
	}
	
	/**
	 * 多表联表查询例子2：固定参数
	 */
	public Page<OrderDetailView> queryOrdersByUserAndShop(
			String operAccount, Date submitTime, String shopName,
			Pageable pageable){
		Page<Object[]> dbresult = exampleOrderRepository.queryOrdersByUserAndShop(
				operAccount, submitTime, shopName, pageable);
		
		//合并多表数据至单个视图对象中
		List<OrderDetailView> content = new ArrayList<>();
		dbresult.forEach(record -> {
			Object[] items = (Object[])record;
			ExampleOrder t1 = (ExampleOrder)items[0];
			ExampleOrderProduct t2 = (ExampleOrderProduct)items[1];
			
			OrderDetailView v = new OrderDetailView();
			try {
				PropertyUtils.copyProperties(v, t1);
				PropertyUtils.copyProperties(v, t2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			content.add(v);
		});
		Page<OrderDetailView> result = new PageImpl<>(content, pageable, dbresult.getTotalElements());
		return result;
	}
	
	@Override
	public List<ExampleOrder> queryDataOnly(ExampleOrderDBParam params, Pageable pageable){
		return exampleOrderRepository.queryDataOnly(params, pageable);
	}
	
	@Override
	public ExampleOrder get(Long id) {
		Optional<ExampleOrder> o = exampleOrderRepository.findById(id);
		if(o.isPresent()) {
			ExampleOrder m = o.get();
			if(m.getOrderStatus() != null) {
				Map<String, SysCode> orderstatus = sysCodeService.getCodeMap("order.status");
				SysCode c = orderstatus.get(m.getOrderStatus().toString());
				if(c != null) {				
					m.setOrderStatusName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录ExampleOrder："+id);
		return null;
	}
	
	@Override
	public ExampleOrder doSave(ExampleOrder m) {
		return exampleOrderRepository.save(m);
	}
	
	@Override
	public void doDelete(Long id) {
		exampleOrderRepository.deleteById(id);
	}
	
	@Override
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			exampleOrderRepository.deleteById(new Long(items[i]));
		}
	}
	
	@Override
	public List<ExampleOrder> selectAll(){
		return orderMapper.selectAll();
	}
	
	@Override
	public List<ExampleOrder> selectAll2(){
		return orderMapper.selectAll2();
	}
	
}
