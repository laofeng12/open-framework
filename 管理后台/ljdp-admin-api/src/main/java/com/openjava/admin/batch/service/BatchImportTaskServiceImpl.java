package com.openjava.admin.batch.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.batch.domain.BatchFileimportTask;
import com.openjava.admin.batch.query.BatchFileimportTaskDBParam;
import com.openjava.admin.batch.repository.BatchImportTaskRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author 何子右
 *
 */
@Service
@Transactional
public class BatchImportTaskServiceImpl implements BatchImportTaskService {
	
	@Resource
	private BatchImportTaskRepository batchImportTaskRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<BatchFileimportTask> query(BatchFileimportTaskDBParam params, Pageable pageable){
		Page<BatchFileimportTask> pageresult = batchImportTaskRepository.query(params, pageable);
		Map<String, SysCode> jobprocessstate = sysCodeService.getCodeMap("job.process.state");
		Map<String, SysCode> jobprocessway = sysCodeService.getCodeMap("job.process.way");
		for (BatchFileimportTask m : pageresult.getContent()) {
			if(m.getProcState() != null) {
				SysCode c = jobprocessstate.get(m.getProcState().toString());
				if(c != null) {
					m.setProcStateName(c.getCodename());
				}
			}
			if(m.getProcWay() != null) {
				SysCode c = jobprocessway.get(m.getProcWay().toString());
				if(c != null) {
					m.setProcWayName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<BatchFileimportTask> queryDataOnly(BatchFileimportTaskDBParam params, Pageable pageable){
		return batchImportTaskRepository.queryDataOnly(params, pageable);
	}
	
	public BatchFileimportTask get(String id) {
		Optional<BatchFileimportTask> o = batchImportTaskRepository.findById(id);
		if(o.isPresent()) {
			BatchFileimportTask m = o.get();
			if(m.getProcState() != null) {
				Map<String, SysCode> jobprocessstate = sysCodeService.getCodeMap("job.process.state");
				SysCode c = jobprocessstate.get(m.getProcState().toString());
				if(c != null) {				
					m.setProcStateName(c.getCodename());
				}
			}
			if(m.getProcWay() != null) {
				Map<String, SysCode> jobprocessway = sysCodeService.getCodeMap("job.process.way");
				SysCode c = jobprocessway.get(m.getProcWay().toString());
				if(c != null) {				
					m.setProcWayName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录BatchFileimportTask："+id);
		return null;
	}
	
	
}
