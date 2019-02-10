package com.openjava.example.order.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.ljdp.core.spring.data.JpaMultiDynamicQueryDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.openjava.example.order.domain.ExampleOrder;
import com.openjava.example.order.domain.ExampleOrderProduct;
import com.openjava.example.order.query.ExampleOrderDBParam;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.vo.OrderDetailView;

/**
 * 动态参数之多表关联查询例子
 * @author hzy
 *
 */
public class ExampleOrderRepositoryImpl implements ExampleOrderRepositoryCustom {
	private EntityManager em;
	private JpaMultiDynamicQueryDAO dao;//动态参数多表关联查询
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
		this.dao = new JpaMultiDynamicQueryDAO(em);
	}
	
	/**
	 * 关联多表查询，返回多表合并后的对象信息
	 * @param orderParams 订单参数
	 * @param prodParams  订单商品明细参数
	 * @param pageable
	 * @return
	 */
	public Page<OrderDetailView> queryOrderProducts(
			ExampleOrderDBParam orderParams,
			ExampleOrderProductDBParam prodParams,
			Pageable pageable){
		//设置动态参数所属的表
		orderParams.setTableAlias("t1");
		prodParams.setTableAlias("t2");
		//编写联表hql
		String multiHql = "select t1,t2 from ExampleOrder t1, ExampleOrderProduct t2 where t1.orderId=t2.orderId";
		//执行查询
		Page<?> dbresult = dao.query(multiHql, pageable, orderParams, prodParams);
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
		//返回标准spring分页对象
		Page<OrderDetailView> result = new PageImpl<>(content, pageable, dbresult.getTotalElements());
		return result;
	}
}
