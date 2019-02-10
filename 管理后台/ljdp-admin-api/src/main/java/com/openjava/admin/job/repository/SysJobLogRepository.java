package com.openjava.admin.job.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.job.domain.SysJobLog;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface SysJobLogRepository extends DynamicJpaRepository<SysJobLog, Long>, SysJobLogRepositoryCustom{
	
}
