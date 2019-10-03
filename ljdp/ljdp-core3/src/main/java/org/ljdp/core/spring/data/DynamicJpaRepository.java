package org.ljdp.core.spring.data;

import java.io.Serializable;
import java.util.List;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DynamicJpaRepository<T, ID extends Serializable> extends
			JpaRepository<T, ID>,JpaSpecificationExecutor<T>  {
	
	/**
	 * 单表动态参数查询（查询条件不固定）
	 * 支持：分页+排序
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @param pageable 分页，例如： new PageRequest(0, 20);
	 * @return
	 */
	public Page<T> query(RoDBQueryParam params, Pageable pageable);
	/**
	 * 限制查询数量，但是不返回分页信息
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<T> queryDataOnly(RoDBQueryParam params, Pageable pageable);
	
	/**
	 * 单表动态参数查询（查询条件不固定）
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public List<T> query(RoDBQueryParam params);
	
	/**
	 * 单表动态参数查询（查询条件不固定）
	 * 支持：排序
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @param sort 排序，例如：
	 * new Sort(new Sort.Order(Sort.Direction.DESC, "operTime"), 
	 * 			new Sort.Order(Sort.Direction.ASC, "orderChannel")
	 * 			)
	 * @return
	 */
	public List<T> query(RoDBQueryParam params, Sort sort);
	
	/**
	 * 单表动态参数总数计算（查询条件不固定）
	 * @param params @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public int count(RoDBQueryParam params);
	
}
