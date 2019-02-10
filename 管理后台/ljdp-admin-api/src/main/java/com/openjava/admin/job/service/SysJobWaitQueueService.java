package com.openjava.admin.job.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.job.domain.SysJobWaitQueue;
import com.openjava.admin.job.query.SysJobWaitQueueDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysJobWaitQueueService {
	public Page<SysJobWaitQueue> query(SysJobWaitQueueDBParam params, Pageable pageable);
	
	public List<SysJobWaitQueue> queryDataOnly(SysJobWaitQueueDBParam params, Pageable pageable);
	
	public SysJobWaitQueue get(Long id);
	
	public SysJobWaitQueue doSave(SysJobWaitQueue m);
	
	public void doDelete(Long id);
	public void doRemove(String ids);
}
