package com.openjava.admin.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.user.domain.SysUser;
import com.openjava.admin.user.query.SysUserDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysUserService {
	public Page<SysUser> query(SysUserDBParam params, Pageable pageable);
	
	public List<SysUser> queryDataOnly(SysUserDBParam params, Pageable pageable);
	
	public SysUser get(Long id);
	
	public SysUser doSave(SysUser m);
	
	public void doDelete(Long id);
	public void doRemove(String ids);
	
	public SysUser findByAccount(String account);
}
