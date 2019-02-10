package com.openjava.admin.batch.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.batch.domain.BatchFileimportTask;

/**
 * 数据库访问层
 * @author 何子右
 *
 */
public interface BatchImportTaskRepository extends DynamicJpaRepository<BatchFileimportTask, String>, BatchImportTaskRepositoryCustom{
	
}
