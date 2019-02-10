package com.openjava.admin.sys.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.sys.domain.SysSubsystem;
import com.openjava.admin.sys.query.SysSubsystemDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysSubsystemService {
	public Page<SysSubsystem> query(SysSubsystemDBParam params, Pageable pageable);
	
	public List<SysSubsystem> queryDataOnly(SysSubsystemDBParam params, Pageable pageable);
	
	public SysSubsystem get(Long id);
	
	public SysSubsystem doSave(SysSubsystem m);
	
	public void doDelete(Long id);
	
	public List<SysSubsystem> findAll();
	
	public List<SysSubsystem> findMySubsystem(Long userid);
}
