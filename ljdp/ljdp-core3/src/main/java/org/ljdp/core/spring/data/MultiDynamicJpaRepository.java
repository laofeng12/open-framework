package org.ljdp.core.spring.data;

import java.io.Serializable;
import java.util.List;

import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface MultiDynamicJpaRepository<T, ID extends Serializable>
		extends DynamicJpaRepository<T, ID> {

	/**
	 * 多表关联动态参数查询
	 * @param jpql  关联的语句， 例如：select u,ur from User u, UserRole ur where u.userid=ur.userid
	 * @param pageable 分页，例如： new PageRequest(0, 20);
	 * @param paramArray @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public Page<?> query(String jpql, Pageable pageable , RoDBQueryParam... paramArray);
	
	/**
	 * 多表关联动态参数查询
	 * @param jpql  关联的语句， 例如：select u,ur from User u, UserRole ur where u.userid=ur.userid
	 * @param sort 排序，例如：
	 * new Sort(new Sort.Order(Sort.Direction.DESC, "operTime"), 
	 * 			new Sort.Order(Sort.Direction.ASC, "orderChannel")
	 * 			)
	 * @param paramArray @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public List<?> query(String jpql, Sort sort, RoDBQueryParam... paramArray);
	
	
	/**
	 * 多表关联动态参数查询
	 * @param jpql  关联的语句， 例如：select u,ur from User u, UserRole ur where u.userid=ur.userid
	 * @param paramArray @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public List<?> query(String jpql, RoDBQueryParam... paramArray);
	
	/**
	 * 多表关联动态参数查询计数
	 * @param jpql  关联的语句， 例如：select u,ur from User u, UserRole ur where u.userid=ur.userid
	 * @param paramArray @see org.opensource.ljdp.core.db.DBQueryParam
	 * @return
	 */
	public int count(String jpql, RoDBQueryParam... paramArray);
	
}
