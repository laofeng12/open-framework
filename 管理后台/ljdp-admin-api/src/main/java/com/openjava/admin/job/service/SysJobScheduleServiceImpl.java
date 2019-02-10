package com.openjava.admin.job.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.job.domain.SysJobSchedule;
import com.openjava.admin.job.query.SysJobScheduleDBParam;
import com.openjava.admin.job.repository.SysJobScheduleRepository;
/**
 * 业务层
 * @author 子右
 *
 */
@Service
@Transactional
public class SysJobScheduleServiceImpl implements SysJobScheduleService {
	
	@Resource
	private SysJobScheduleRepository sysJobScheduleRepository;
	
	public Page<SysJobSchedule> query(SysJobScheduleDBParam params, Pageable pageable){
		Page<SysJobSchedule> pageresult = sysJobScheduleRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<SysJobSchedule> queryDataOnly(SysJobScheduleDBParam params, Pageable pageable){
		return sysJobScheduleRepository.queryDataOnly(params, pageable);
	}
	
	public SysJobSchedule get(String id) {
		Optional<SysJobSchedule> o = sysJobScheduleRepository.findById(id);
		if(o.isPresent()) {
			SysJobSchedule m = o.get();
			return m;
		}
		System.out.println("找不到记录SysJobSchedule："+id);
		return null;
	}
	
	public SysJobSchedule doSave(SysJobSchedule m) {
		return sysJobScheduleRepository.save(m);
	}
	
//	public void doDelete(String id) {
//		sysJobScheduleRepository.deleteById(id);
//	}
//	public void doRemove(String ids) {
//		String[] items = ids.split(",");
//		for (int i = 0; i < items.length; i++) {
//			sysJobScheduleRepository.deleteById(new String(items[i]));
//		}
//	}
	
	public SysJobSchedule findByJobClassAndJobMethod(String jobClass, String jobMethod) {
		return sysJobScheduleRepository.findByJobClassAndJobMethod(jobClass, jobMethod);
	}
}
