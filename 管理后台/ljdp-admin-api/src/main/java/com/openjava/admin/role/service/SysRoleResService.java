package com.openjava.admin.role.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.role.domain.SysRoleRes;
import com.openjava.admin.role.query.SysRoleResDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysRoleResService {
	public Page<SysRoleRes> query(SysRoleResDBParam params, Pageable pageable);
	
	public List<SysRoleRes> queryDataOnly(SysRoleResDBParam params, Pageable pageable);
	
	public SysRoleRes get(Long id);
	
	/**
	 * 查询角色下面的资源列表
	 * @param roleid
	 * @return
	 */
	public List<SysRoleRes> findByRoleid(Long roleid);
}
