package com.openjava.admin.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.user.domain.SysUserRole;
import com.openjava.admin.user.query.SysUserRoleDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysUserRoleService {
	public Page<SysUserRole> query(SysUserRoleDBParam params, Pageable pageable);
	
	public List<SysUserRole> queryDataOnly(SysUserRoleDBParam params, Pageable pageable);
	
	public SysUserRole get(Long id);
	
	public SysUserRole doSave(SysUserRole m);
	
	public void doDelete(Long id);
	
	public List<SysRole> findRoleByUser(Long userid);
}
