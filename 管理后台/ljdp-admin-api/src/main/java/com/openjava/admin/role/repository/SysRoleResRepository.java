package com.openjava.admin.role.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.role.domain.SysRoleRes;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysRoleResRepository extends DynamicJpaRepository<SysRoleRes, Long>, SysRoleResRepositoryCustom{
	
	/**
	 * 查询角色下面的资源列表
	 * @param roleid
	 * @return
	 */
	public List<SysRoleRes> findByRoleid(Long roleid);
	

	@Modifying
	@Query("delete from SysRoleRes t where t.roleid=:roleid")
	public int deleteByRoleid(@Param("roleid")Long roleid);
}
