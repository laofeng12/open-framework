package com.openjava.admin.role.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.role.domain.SysRole;
import com.openjava.admin.role.domain.SysRoleRes;
import com.openjava.admin.role.query.SysRoleDBParam;
import com.openjava.admin.role.repository.SysRoleRepository;
import com.openjava.admin.role.repository.SysRoleResRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysRoleServiceImpl implements SysRoleService {
	
	@Resource
	private SysRoleRepository sysRoleRepository;
	@Resource
	private SysCodeService sysCodeService;
	@Resource
	private SysRoleResRepository sysRoleResRepository;
	
	public Page<SysRole> query(SysRoleDBParam params, Pageable pageable){
		Page<SysRole> pageresult = sysRoleRepository.query(params, pageable);
		Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
		for (SysRole m : pageresult.getContent()) {
			if(m.getAllowdel() != null) {
				SysCode c = publicYN.get(m.getAllowdel().toString());
				if(c != null) {
					m.setAllowdelName(c.getCodename());
				}
			}
			if(m.getAllowedit() != null) {
				SysCode c = publicYN.get(m.getAllowedit().toString());
				if(c != null) {
					m.setAlloweditName(c.getCodename());
				}
			}
			if(m.getEnabled() != null) {
				SysCode c = publicYN.get(m.getEnabled().toString());
				if(c != null) {
					m.setEnabledName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<SysRole> queryDataOnly(SysRoleDBParam params, Pageable pageable){
		return sysRoleRepository.queryDataOnly(params, pageable);
	}
	
	public SysRole get(Long id) {
		Optional<SysRole> o = sysRoleRepository.findById(id);
		if(o.isPresent()) {
			SysRole m = o.get();
			if(m.getAllowdel() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getAllowdel().toString());
				m.setAllowdelName(c.getCodename());
			}
			if(m.getAllowedit() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getAllowedit().toString());
				m.setAlloweditName(c.getCodename());
			}
			if(m.getEnabled() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getEnabled().toString());
				m.setEnabledName(c.getCodename());
			}
			return m;
		}
		System.out.println("找不到菜单："+id);
		return null;
	}
	
	public SysRole doSave(SysRole m) {
		return sysRoleRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysRoleRepository.deleteById(id);
	}
	
	/**
	 * 更新角色资源权限
	 * @param roleid
	 * @param resIds
	 */
	public void updateRoleRes(Long roleid, String resIds) {
		Optional<SysRole> o = sysRoleRepository.findById(roleid);
		if(!o.isPresent()) {
			return;
		}
		SysRole role = o.get();
		int del = sysRoleResRepository.deleteByRoleid(roleid);
		SequenceService ss = ConcurrentSequence.getInstance();
		String[] items = resIds.split(",");
		for (int i = 0; i < items.length; i++) {
			SysRoleRes rr = new SysRoleRes();
			rr.setRoleresid(ss.getSequence());
			rr.setRoleid(roleid);
			rr.setResid(new Long(items[i]));
			rr.setSystemid(role.getSystemid());
			sysRoleResRepository.save(rr);
		}
		System.out.println("更新角色权限：name="+role.getRolename()+",del="+del+",new="+items.length);
	}
}
