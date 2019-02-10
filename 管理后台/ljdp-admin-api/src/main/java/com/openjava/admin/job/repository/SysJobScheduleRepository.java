package com.openjava.admin.job.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.job.domain.SysJobSchedule;

/**
 * 数据库访问层
 * @author 子右
 *
 */
public interface SysJobScheduleRepository extends DynamicJpaRepository<SysJobSchedule, String>, SysJobScheduleRepositoryCustom{
	
	public SysJobSchedule findByJobClassAndJobMethod(String jobClass, String jobMethod);
}
