package com.openjava.admin.res.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.res.domain.SysRes;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysResRepository extends DynamicJpaRepository<SysRes, Long>, SysResRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysRes t where t.resid=:resid")
	public int deleteByPkId(@Param("resid")Long resid);
	
	@Query("select r from SysRes r, SysRoleRes rr, SysUserRole ur where "
			+ "r.resid=rr.resid and rr.roleid=ur.roleid and ur.userid=:userid "
			+ "and (r.isdisplayinmenu=1 or r.isfolder=1)"
			+ " order by r.parentid, r.sort")
	public List<SysRes> findMyRes(@Param("userid")Long userid);
	
	/**
	  * 进行排序后查询
	 * @return
	 */
	@Query("from SysRes t order by t.parentid,t.sort")
	public List<SysRes> findAllInSort();
}
