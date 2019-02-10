package com.openjava.admin.job.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.job.domain.SysJobLog;
import com.openjava.admin.job.query.SysJobLogDBParam;
import com.openjava.admin.job.repository.SysJobLogRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysJobLogServiceImpl implements SysJobLogService {
	
	@Resource
	private SysJobLogRepository sysJobLogRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<SysJobLog> query(SysJobLogDBParam params, Pageable pageable){
		Page<SysJobLog> pageresult = sysJobLogRepository.query(params, pageable);
		Map<String, SysCode> schedulejobstatus = sysCodeService.getCodeMap("schedule.job.status");
		for (SysJobLog m : pageresult.getContent()) {
			if(m.getStatus() != null) {
				SysCode c = schedulejobstatus.get(m.getStatus().toString());
				if(c != null) {
					m.setStatusName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<SysJobLog> queryDataOnly(SysJobLogDBParam params, Pageable pageable){
		return sysJobLogRepository.queryDataOnly(params, pageable);
	}
	
	public SysJobLog get(Long id) {
		Optional<SysJobLog> o = sysJobLogRepository.findById(id);
		if(o.isPresent()) {
			SysJobLog m = o.get();
			if(m.getStatus() != null) {
				Map<String, SysCode> schedulejobstatus = sysCodeService.getCodeMap("schedule.job.status");
				SysCode c = schedulejobstatus.get(m.getStatus().toString());
				if(c != null) {				
					m.setStatusName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录SysJobLog："+id);
		return null;
	}
	
	public SysJobLog doSave(SysJobLog m) {
		return sysJobLogRepository.save(m);
	}
	
}
