package com.openjava.admin.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.user.domain.SysUserOrg;
import com.openjava.admin.user.query.SysUserOrgDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysUserOrgService {
	public Page<SysUserOrg> query(SysUserOrgDBParam params, Pageable pageable);
	
	public List<SysUserOrg> queryDataOnly(SysUserOrgDBParam params, Pageable pageable);
	
	public SysUserOrg get(Long id);
	
	public SysUserOrg doSave(SysUserOrg m);
	
	public void doDelete(Long id);
	
	public List<SysOrg> findOrgByUser(Long userid);
}
