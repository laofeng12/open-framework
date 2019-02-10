package com.openjava.admin.user.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.user.domain.SysUserOrg;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysUserOrgRepository extends DynamicJpaRepository<SysUserOrg, Long>, SysUserOrgRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysUserOrg t where t.userorgid=:userorgid")
	public int deleteByPkId(@Param("userorgid")Long userorgid);
	
	@Query("select t1 from SysOrg t1, SysUserOrg t2 where t1.orgid=t2.orgid and t2.userid=:userid")
	public List<SysOrg> findOrgByUser(@Param("userid")Long userid);
}
