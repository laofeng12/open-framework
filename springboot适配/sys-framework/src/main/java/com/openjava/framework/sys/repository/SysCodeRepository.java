package com.openjava.framework.sys.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.framework.sys.domain.SysCode;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysCodeRepository extends DynamicJpaRepository<SysCode, Integer>, SysCodeRepositoryCustom{
	
	public List<SysCode> findByCodetype(String codetype);
	
	public List<SysCode> findByCodetype(String codetype, Sort sort);
	
	@Query("from SysCode where codeDef=:codeDef and codetype=:codetype")
	public List<SysCode> findByCodeAndType(@Param("codeDef")String codeDef, @Param("codetype")String codetype);
}
