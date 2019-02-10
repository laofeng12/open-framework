package org.ljdp.support.batch.service;

import java.util.List;

import javax.annotation.Resource;

import org.ljdp.plugin.batch.persistent.BtFileImportTask;
import org.ljdp.plugin.batch.persistent.BtFileImportTaskDBParam;
import org.ljdp.support.batch.repository.BatchFileimportTaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class BatchFileimportTaskServiceImpl implements BatchFileimportTaskService {
	
	@Resource
	private BatchFileimportTaskRepository batchFileimportTaskRepository;
	
	public Page<BtFileImportTask> query(BtFileImportTaskDBParam params, Pageable pageable){
		return batchFileimportTaskRepository.query(params, pageable);
	}
	
	public List<BtFileImportTask> queryDataOnly(BtFileImportTaskDBParam params, Pageable pageable){
		return batchFileimportTaskRepository.queryDataOnly(params, pageable);
	}
	
	public BtFileImportTask get(String id) {
		return batchFileimportTaskRepository.findById(id).get();
	}
	
	
}
