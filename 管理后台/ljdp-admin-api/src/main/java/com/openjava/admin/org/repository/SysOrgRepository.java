package com.openjava.admin.org.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.org.domain.SysOrg;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysOrgRepository extends DynamicJpaRepository<SysOrg, Long>, SysOrgRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysOrg t where t.orgid=:orgid")
	public int deleteByPkId(@Param("orgid")Long orgid);
}
