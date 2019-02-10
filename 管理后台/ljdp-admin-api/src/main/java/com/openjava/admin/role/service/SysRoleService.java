package com.openjava.admin.role.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.role.query.SysRoleDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysRoleService {
	public Page<SysRole> query(SysRoleDBParam params, Pageable pageable);
	
	public List<SysRole> queryDataOnly(SysRoleDBParam params, Pageable pageable);
	
	public SysRole get(Long id);
	
	public SysRole doSave(SysRole m);
	
	public void doDelete(Long id);
	
	/**
	 * 更新角色资源权限
	 * @param roleid
	 * @param resIds
	 */
	public void updateRoleRes(Long roleid, String resIds);
}
