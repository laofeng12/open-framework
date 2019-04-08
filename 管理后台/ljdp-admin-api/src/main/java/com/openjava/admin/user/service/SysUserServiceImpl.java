package com.openjava.admin.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.user.domain.SysUser;
import com.openjava.admin.user.query.SysUserDBParam;
import com.openjava.admin.user.repository.SysUserRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {
	
	@Resource
	private SysUserRepository sysUserRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<SysUser> query(SysUserDBParam params, Pageable pageable){
		Page<SysUser> pageresult = sysUserRepository.query(params, pageable);
		Map<String, SysCode> sysaccounttype = sysCodeService.getCodeMap("sys.account.type");
		Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
		Map<String, SysCode> sysuserstate = sysCodeService.getCodeMap("sys.user.state");
		Map<String, SysCode> sysuserfromtype = sysCodeService.getCodeMap("sys.user.fromtype");
		for (SysUser m : pageresult.getContent()) {
			if(m.getAccounttype() != null) {
				SysCode c = sysaccounttype.get(m.getAccounttype().toString());
				if(c != null) {
					m.setAccounttypeName(c.getCodename());
				}
			}
			if(m.getIsexpired() != null) {
				SysCode c = publicYN.get(m.getIsexpired().toString());
				if(c != null) {
					m.setIsexpiredName(c.getCodename());
				}
			}
			if(m.getIslock() != null) {
				SysCode c = publicYN.get(m.getIslock().toString());
				if(c != null) {
					m.setIslockName(c.getCodename());
				}
			}
			if(m.getStatus() != null) {
				SysCode c = sysuserstate.get(m.getStatus().toString());
				if(c != null) {
					m.setStatusName(c.getCodename());
				}
			}
			if(m.getFromtype() != null) {
				SysCode c = sysuserfromtype.get(m.getFromtype().toString());
				if(c != null) {
					m.setFromtypeName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<SysUser> queryDataOnly(SysUserDBParam params, Pageable pageable){
		return sysUserRepository.queryDataOnly(params, pageable);
	}
	
	public SysUser get(Long id) {
		Optional<SysUser> o = sysUserRepository.findById(id);
		if(!o.isPresent()) {
			return null;
		}
		SysUser m = o.get();
		if(m.getAccounttype() != null) {
			Map<String, SysCode> sysaccounttype = sysCodeService.getCodeMap("sys.account.type");
			SysCode c = sysaccounttype.get(m.getAccounttype().toString());
			if(c != null) {
				m.setAccounttypeName(c.getCodename());
			}
		}
		if(m.getIsexpired() != null) {
			Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
			SysCode c = publicYN.get(m.getIsexpired().toString());
			if(c != null) {
				m.setIsexpiredName(c.getCodename());
			}
		}
		if(m.getIslock() != null) {
			Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
			SysCode c = publicYN.get(m.getIslock().toString());
			if(c != null) {
				m.setIslockName(c.getCodename());
			}
		}
		if(m.getStatus() != null) {
			Map<String, SysCode> sysuserstate = sysCodeService.getCodeMap("sys.user.state");
			SysCode c = sysuserstate.get(m.getStatus().toString());
			if(c != null) {				
				m.setStatusName(c.getCodename());
			}
		}
		if(m.getFromtype() != null) {
			Map<String, SysCode> sysuserfromtype = sysCodeService.getCodeMap("sys.user.fromtype");
			SysCode c = sysuserfromtype.get(m.getFromtype().toString());
			if(c != null) {
				m.setFromtypeName(c.getCodename());
			}
		}
		return m;
	}
	
	public SysUser doSave(SysUser m) {
		return sysUserRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysUserRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			sysUserRepository.deleteById(new Long(items[i]));
		}
	}
	
	public SysUser findByAccount(String account) {
		return sysUserRepository.findByAccount(account);
	}

	public SysUser findByMobile(String mobile){
		return sysUserRepository.findByMobile(mobile);
	};
}
