package com.openjava.admin.role.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.role.domain.SysRole;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysRoleRepository extends DynamicJpaRepository<SysRole, Long>, SysRoleRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysRole t where t.roleid=:roleid")
	public int deleteByPkId(@Param("roleid")Long roleid);
}
