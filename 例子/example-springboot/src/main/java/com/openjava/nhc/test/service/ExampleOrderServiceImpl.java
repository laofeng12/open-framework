package com.openjava.nhc.test.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.nhc.test.domain.ExampleOrder;
import com.openjava.nhc.test.query.ExampleOrderDBParam;
import com.openjava.nhc.test.repository.ExampleOrderRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 订单管理业务层
 * @author 自由
 *
 */
@Service
@Transactional
public class ExampleOrderServiceImpl implements ExampleOrderService {
	
	@Resource
	private ExampleOrderRepository exampleOrderRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<ExampleOrder> query(ExampleOrderDBParam params, Pageable pageable){
		Page<ExampleOrder> pageresult = exampleOrderRepository.query(params, pageable);
		Map<String, SysCode> publicenable = sysCodeService.getCodeMap("public.enable");
		for (ExampleOrder m : pageresult.getContent()) {
			if(m.getOrderStatus() != null) {
				SysCode c = publicenable.get(m.getOrderStatus().toString());
				if(c != null) {
					m.setOrderStatusName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<ExampleOrder> queryDataOnly(ExampleOrderDBParam params, Pageable pageable){
		return exampleOrderRepository.queryDataOnly(params, pageable);
	}
	
	public ExampleOrder get(String id) {
		Optional<ExampleOrder> o = exampleOrderRepository.findById(id);
		if(o.isPresent()) {
			ExampleOrder m = o.get();
			if(m.getOrderStatus() != null) {
				Map<String, SysCode> publicenable = sysCodeService.getCodeMap("public.enable");
				SysCode c = publicenable.get(m.getOrderStatus().toString());
				if(c != null) {				
					m.setOrderStatusName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录ExampleOrder："+id);
		return null;
	}
	
	public ExampleOrder doSave(ExampleOrder m) {
		return exampleOrderRepository.save(m);
	}
	
	public void doDelete(String id) {
		exampleOrderRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			exampleOrderRepository.deleteById(new String(items[i]));
		}
	}
}
