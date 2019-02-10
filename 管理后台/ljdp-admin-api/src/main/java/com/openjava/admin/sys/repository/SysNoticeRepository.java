package com.openjava.admin.sys.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.sys.domain.SysNotice;

/**
 * 数据库访问层
 * @author heizyou
 *
 */
public interface SysNoticeRepository extends DynamicJpaRepository<SysNotice, String>, SysNoticeRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete SysNotice t where t.nid=:nid")
	public int deleteByPkId(@Param("nid")String nid);
	
	//发布
	@Modifying
	@Query("update SysNotice t set t.nstatus='3' where t.nid in(:nidList) and t.nstatus in('2','4')")
	public int publish(@Param("nidList")List<String> nidList);
	
	//下架
	@Modifying
	@Query("update SysNotice t set t.nstatus='4' where t.nid in(:nidList) and t.nstatus='3'")
	public int downNotic(@Param("nidList")List<String> nidList);
	
	//审核通过
	@Modifying
	@Query("update SysNotice t set t.nstatus='2' where t.nid=:nid and t.nstatus='1'")
	public int auditPass(@Param("nid")String nid);
	//审核不通过
	@Modifying
	@Query("update SysNotice t set t.nstatus='5' where t.nid=:nid and t.nstatus='1'")
	public int auditNotPass(@Param("nid")String nid);
	
	/**
	 * 查询已经发布的通知
	 * @param pageable
	 * @return
	 */
	@Query("from SysNotice t where t.nstatus='3'")
	public List<SysNotice> findByPublish(Pageable pageable);
}
