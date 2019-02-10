package com.openjava.admin.role.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.role.domain.SysRoleRes;
import com.openjava.admin.role.query.SysRoleResDBParam;
import com.openjava.admin.role.repository.SysRoleResRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysRoleResServiceImpl implements SysRoleResService {
	
	@Resource
	private SysRoleResRepository sysRoleResRepository;
	
	public Page<SysRoleRes> query(SysRoleResDBParam params, Pageable pageable){
		Page<SysRoleRes> pageresult = sysRoleResRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<SysRoleRes> queryDataOnly(SysRoleResDBParam params, Pageable pageable){
		return sysRoleResRepository.queryDataOnly(params, pageable);
	}
	
	public SysRoleRes get(Long id) {
		Optional<SysRoleRes> m = sysRoleResRepository.findById(id);
		return m.get();
	}
	
	public List<SysRoleRes> findByRoleid(Long roleid){
		return sysRoleResRepository.findByRoleid(roleid);
	}
}
