package com.openjava.nhc.test.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import com.openjava.nhc.test.domain.ExampleOrder;
import com.openjava.nhc.test.query.ExampleOrderDBParam;
import com.openjava.nhc.test.repository.ExampleOrderRepository;
import com.openjava.nhc.test.repository.SpecificationOrderRepository;


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
	private SpecificationOrderRepository specificationOrderRepository;
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
	
	public Page<ExampleOrder> query2(ExampleOrderDBParam params, Pageable pageable){
		
		Page<ExampleOrder> pageresult = specificationOrderRepository.findAll(new Specification<ExampleOrder>() {
			
			@Override
			public Predicate toPredicate(Root<ExampleOrder> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if(StringUtils.isNotBlank(params.getLike_operAccount())) {
					predicates.add(criteriaBuilder.like(root.get("operAccount"), "%"+params.getLike_operAccount()+"%"));
				}
				if(StringUtils.isNotBlank(params.getLike_userName())) {
					predicates.add(criteriaBuilder.like(root.get("userName"), "%"+params.getLike_userName()+"%"));
				}
				if(params.getEq_orderStatus() != null) {
					predicates.add(criteriaBuilder.equal(root.get("orderStatus"), params.getEq_orderStatus()));
				}
				if(params.getGe_submitTime() != null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("submitTime"), params.getGe_submitTime()));
				}
				if(params.getLe_submitTime() != null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("submitTime"), params.getLe_submitTime()));
				}
				if(params.getEq_totalPrice() != null) {
					predicates.add(criteriaBuilder.equal(root.get("totalPrice"), params.getEq_totalPrice()));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageable);
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
	
	@Transactional(rollbackFor=Exception.class)
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
