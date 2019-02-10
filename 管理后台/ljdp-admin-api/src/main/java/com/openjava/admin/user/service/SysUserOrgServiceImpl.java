package com.openjava.admin.user.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.org.domain.SysOrg;
import com.openjava.admin.org.repository.SysOrgRepository;
import com.openjava.admin.user.domain.SysUserOrg;
import com.openjava.admin.user.query.SysUserOrgDBParam;
import com.openjava.admin.user.repository.SysUserOrgRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysUserOrgServiceImpl implements SysUserOrgService {
	
	@Resource
	private SysUserOrgRepository sysUserOrgRepository;
	@Resource
	private SysOrgRepository sysOrgRepository;
	
	public Page<SysUserOrg> query(SysUserOrgDBParam params, Pageable pageable){
		Page<SysUserOrg> pageresult = sysUserOrgRepository.query(params, pageable);
		//翻译组织名
		pageresult.forEach(item -> {
			SysOrg org = sysOrgRepository.getOne(item.getOrgid());
			if(org != null) {
				item.setOrgname(org.getOrgname());
				item.setOrgpathname(org.getOrgpathname());
			}
		});
		return pageresult;
	}
	
	public List<SysUserOrg> queryDataOnly(SysUserOrgDBParam params, Pageable pageable){
		return sysUserOrgRepository.queryDataOnly(params, pageable);
	}
	
	public SysUserOrg get(Long id) {
		Optional<SysUserOrg> m = sysUserOrgRepository.findById(id);
		return m.get();
	}
	
	public SysUserOrg doSave(SysUserOrg m) {
		return sysUserOrgRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysUserOrgRepository.deleteById(id);
	}
	
	public List<SysOrg> findOrgByUser(Long userid) {
		return sysUserOrgRepository.findOrgByUser(userid);
	}
}
