package com.openjava.admin.job.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.job.domain.SysJobWaitQueue;
import com.openjava.admin.job.query.SysJobWaitQueueDBParam;
import com.openjava.admin.job.repository.SysJobWaitQueueRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysJobWaitQueueServiceImpl implements SysJobWaitQueueService {
	
	@Resource
	private SysJobWaitQueueRepository sysJobWaitQueueRepository;
	
	public Page<SysJobWaitQueue> query(SysJobWaitQueueDBParam params, Pageable pageable){
		Page<SysJobWaitQueue> pageresult = sysJobWaitQueueRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<SysJobWaitQueue> queryDataOnly(SysJobWaitQueueDBParam params, Pageable pageable){
		return sysJobWaitQueueRepository.queryDataOnly(params, pageable);
	}
	
	public SysJobWaitQueue get(Long id) {
		Optional<SysJobWaitQueue> o = sysJobWaitQueueRepository.findById(id);
		if(o.isPresent()) {
			SysJobWaitQueue m = o.get();
			return m;
		}
		System.out.println("找不到记录SysJobWaitQueue："+id);
		return null;
	}
	
	public SysJobWaitQueue doSave(SysJobWaitQueue m) {
		return sysJobWaitQueueRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysJobWaitQueueRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			sysJobWaitQueueRepository.deleteById(new Long(items[i]));
		}
	}
}
