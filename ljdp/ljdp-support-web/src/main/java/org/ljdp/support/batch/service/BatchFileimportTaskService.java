package org.ljdp.support.batch.service;

import java.util.List;

import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.persistent.BtFileImportTaskDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 业务层接口
 * @author hzy
 *
 */
public interface BatchFileimportTaskService {
	public Page<BtFileImportTask> query(BtFileImportTaskDBParam params, Pageable pageable);
	
	public List<BtFileImportTask> queryDataOnly(BtFileImportTaskDBParam params, Pageable pageable);
	
	public BtFileImportTask get(String id);
	
	
}
