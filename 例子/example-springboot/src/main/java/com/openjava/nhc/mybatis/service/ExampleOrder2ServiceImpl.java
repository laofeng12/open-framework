package com.openjava.nhc.mybatis.service;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import com.openjava.nhc.mybatis.domain.ExampleOrderEntity;
import com.openjava.nhc.mybatis.mapper.ExampleOrderMapper;
import com.openjava.nhc.test.query.ExampleOrderDBParam;

@Service
public class ExampleOrder2ServiceImpl extends ServiceImpl<ExampleOrderMapper, ExampleOrderEntity> implements IExampleOrderService {

	@Resource
	private SysCodeService sysCodeService;
	
	public IPage<ExampleOrderEntity> query(ExampleOrderDBParam params, Pageable pageable){
		//JPA的page从0开始，MyBatisPlus的page从1开始。前端统一用从0开始。所以这里需+1
		IPage<ExampleOrderEntity> mypage = new Page<>(pageable.getPageNumber()+1, pageable.getPageSize());
		
		IPage<ExampleOrderEntity> page = this.page(
				mypage,
				new QueryWrapper<ExampleOrderEntity>()
					.like(StringUtils.isNotBlank(params.getLike_operAccount()),"oper_account", params.getLike_operAccount())
					.like(StringUtils.isNotBlank(params.getLike_userName()),"user_name", params.getLike_userName())
					.ge(params.getGe_submitTime() != null, "submit_time", params.getGe_submitTime())
					.le(params.getLe_submitTime() != null, "submit_time", params.getLe_submitTime())
					.eq(params.getEq_totalPrice() != null, "total_price", params.getEq_totalPrice())
					.eq(params.getEq_orderStatus() != null, "order_status", params.getEq_orderStatus())
			);
		
		Map<String, SysCode> orderStatusName = sysCodeService.getCodeMap("orderStatus");
		for (ExampleOrderEntity m : page.getRecords()) {
			if(m.getOrderStatus() != null) {
				SysCode c = orderStatusName.get(m.getOrderStatus().toString());
				if(c != null) {
					m.setOrderStatusName(c.getCodename());
				}
			}
		}
		
		return page;
	}
}
