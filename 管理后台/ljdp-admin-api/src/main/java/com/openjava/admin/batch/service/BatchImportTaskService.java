package com.openjava.admin.batch.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.batch.domain.BatchFileimportTask;
import com.openjava.admin.batch.query.BatchFileimportTaskDBParam;

/**
 * 业务层接口
 * @author 何子右
 *
 */
public interface BatchImportTaskService {
	public Page<BatchFileimportTask> query(BatchFileimportTaskDBParam params, Pageable pageable);
	
	public List<BatchFileimportTask> queryDataOnly(BatchFileimportTaskDBParam params, Pageable pageable);
	
	public BatchFileimportTask get(String id);
	
	
}
