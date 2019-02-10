package com.openjava.admin.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.role.repository.SysRoleRepository;
import com.openjava.admin.user.domain.SysUserRole;
import com.openjava.admin.user.query.SysUserRoleDBParam;
import com.openjava.admin.user.repository.SysUserRoleRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysUserRoleServiceImpl implements SysUserRoleService {
	
	@Resource
	private SysUserRoleRepository sysUserRoleRepository;
	@Resource
	private SysRoleRepository sysRoleRepository;
	
	public Page<SysUserRole> query(SysUserRoleDBParam params, Pageable pageable){
		Page<SysUserRole> pageresult = sysUserRoleRepository.query(params, pageable);
		pageresult.forEach(item -> {
			SysRole r = sysRoleRepository.getOne(item.getRoleid());
			if(r != null) {
				item.setRolename(r.getRolename());
			}
		});
		return pageresult;
	}
	
	public List<SysUserRole> queryDataOnly(SysUserRoleDBParam params, Pageable pageable){
		return sysUserRoleRepository.queryDataOnly(params, pageable);
	}
	
	public SysUserRole get(Long id) {
		Optional<SysUserRole> m = sysUserRoleRepository.findById(id);
		return m.get();
	}
	
	public SysUserRole doSave(SysUserRole m) {
		return sysUserRoleRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysUserRoleRepository.deleteById(id);
	}
	
	public List<SysRole> findRoleByUser(Long userid){
		return sysUserRoleRepository.findRoleByUser(userid);
	}
}
