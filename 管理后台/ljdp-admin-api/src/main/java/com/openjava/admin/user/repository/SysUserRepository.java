package com.openjava.admin.user.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.user.domain.SysUser;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysUserRepository extends DynamicJpaRepository<SysUser, Long>, SysUserRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysUser t where t.userid=:userid")
	public int deleteByPkId(@Param("userid")Long userid);
	
	public SysUser findByAccount(String account);

	public SysUser findByMobile(String mobile);
}
