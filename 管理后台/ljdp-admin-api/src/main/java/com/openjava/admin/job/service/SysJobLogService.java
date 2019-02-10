package com.openjava.admin.job.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.job.domain.SysJobLog;
import com.openjava.admin.job.query.SysJobLogDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysJobLogService {
	public Page<SysJobLog> query(SysJobLogDBParam params, Pageable pageable);
	
	public List<SysJobLog> queryDataOnly(SysJobLogDBParam params, Pageable pageable);
	
	public SysJobLog get(Long id);
	
	public SysJobLog doSave(SysJobLog m);
	
}
