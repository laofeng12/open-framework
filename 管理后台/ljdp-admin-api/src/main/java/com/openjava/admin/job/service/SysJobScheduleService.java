package com.openjava.admin.job.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.job.domain.SysJobSchedule;
import com.openjava.admin.job.query.SysJobScheduleDBParam;

/**
 * 业务层接口
 * @author 子右
 *
 */
public interface SysJobScheduleService {
	public Page<SysJobSchedule> query(SysJobScheduleDBParam params, Pageable pageable);
	
	public List<SysJobSchedule> queryDataOnly(SysJobScheduleDBParam params, Pageable pageable);
	
	public SysJobSchedule get(String id);
	
	public SysJobSchedule doSave(SysJobSchedule m);
	
//	public void doDelete(String id);
//	public void doRemove(String ids);
	
	public SysJobSchedule findByJobClassAndJobMethod(String jobClass, String jobMethod);
}
