package org.ljdp.support.batch.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface BatchFileimportTaskRepository extends DynamicJpaRepository<BtFileImportTask, String>, BatchFileimportTaskRepositoryCustom{
}
