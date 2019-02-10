package com.openjava.admin.sys.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.sys.domain.SysSubsystem;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysSubsystemRepository extends DynamicJpaRepository<SysSubsystem, Long>, SysSubsystemRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysSubsystem t where t.systemid=:systemid")
	public int deleteByPkId(@Param("systemid")Long systemid);
	
	@Query("select ss from SysSubsystem ss where ss.systemid in("
			+ "select distinct r.systemid "
			+ "from SysRes r, SysRoleRes rr, SysUserRole ur "
			+ "where r.resid=rr.resid and rr.roleid=ur.roleid"
			+ " and r.parentid=0 and ur.userid=:userid"
			+ ")")
	public List<SysSubsystem> findMySubsystem(@Param("userid")Long userid);
}
