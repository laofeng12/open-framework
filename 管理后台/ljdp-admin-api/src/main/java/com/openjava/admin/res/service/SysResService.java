package com.openjava.admin.res.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.res.domain.SysRes;
import com.openjava.admin.res.query.SysResDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysResService {
	public Page<SysRes> query(SysResDBParam params, Pageable pageable);
	
	public List<SysRes> queryDataOnly(SysResDBParam params, Pageable pageable);
	
	public SysRes get(Long id);
	
	public SysRes doSave(SysRes m);
	
	public void doDelete(Long id);
	
	public List<SysRes> findAll();
	
	/**
	 * 获取完整路径信息
	 * @param pathId 完整路径id
	 * @param parentid 上级节点id
	 * @return
	 * @throws Exception
	 */
	public String getFullPath(String pathId, Long parentid);
	
	public List<SysRes> findMyRes(Long userid);
	
	public List<SysRes> findAllInSort();
}
