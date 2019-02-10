package com.openjava.admin.user.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.user.domain.SysUserRole;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysUserRoleRepository extends DynamicJpaRepository<SysUserRole, Long>, SysUserRoleRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysUserRole t where t.userroleid=:userroleid")
	public int deleteByPkId(@Param("userroleid")Long userroleid);
	
	@Query("select r from SysRole r, SysUserRole ur where r.roleid=ur.roleid and ur.userid=:userid")
	public List<SysRole> findRoleByUser(@Param("userid")Long userid);
}
