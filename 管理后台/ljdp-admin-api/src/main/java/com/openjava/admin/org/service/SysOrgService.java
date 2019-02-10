package com.openjava.admin.org.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.org.query.SysOrgDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysOrgService {
	public Page<SysOrg> query(SysOrgDBParam params, Pageable pageable);
	
	public List<SysOrg> queryDataOnly(SysOrgDBParam params, Pageable pageable);
	
	public SysOrg get(Long id);
	
	public SysOrg doSave(SysOrg m);
	
	public void doDelete(Long id);
	
	public List<SysOrg> findAll();
	
	/**
	 * 获取组织完整路径信息
	 * @param pathId 完整路径id
	 * @param fullName 完整路径名称
	 * @param orgsupid 上级节点id
	 * @return
	 * @throws Exception
	 */
	public String[] getFullPath(String pathId, String fullName, Long orgsupid);
}
