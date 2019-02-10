package com.openjava.admin.sys.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.sys.domain.SysSubsystem;
import com.openjava.admin.sys.query.SysSubsystemDBParam;
import com.openjava.admin.sys.repository.SysSubsystemRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysSubsystemServiceImpl implements SysSubsystemService {
	
	@Resource
	private SysSubsystemRepository sysSubsystemRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<SysSubsystem> query(SysSubsystemDBParam params, Pageable pageable){
		Page<SysSubsystem> pageresult = sysSubsystemRepository.query(params, pageable);
		Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
		for (SysSubsystem m : pageresult.getContent()) {
			if(m.getAllowdel() != null) {
				SysCode c = publicYN.get(m.getAllowdel().toString());
				if(c != null) {
					m.setAllowdelName(c.getCodename());
				}
			}
			if(m.getNeedorg() != null) {
				SysCode c = publicYN.get(m.getNeedorg().toString());
				if(c != null) {
					m.setNeedorgName(c.getCodename());
				}
			}
			if(m.getIsactive() != null) {
				SysCode c = publicYN.get(m.getIsactive().toString());
				if(c != null) {
					m.setIsactiveName(c.getCodename());
				}
			}
			if(m.getIslocal() != null) {
				SysCode c = publicYN.get(m.getIslocal().toString());
				if(c != null) {
					m.setIslocalName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<SysSubsystem> queryDataOnly(SysSubsystemDBParam params, Pageable pageable){
		return sysSubsystemRepository.queryDataOnly(params, pageable);
	}
	
	public SysSubsystem get(Long id) {
		Optional<SysSubsystem> o = sysSubsystemRepository.findById(id);
		if(o.isPresent()) {
			SysSubsystem m = o.get();
			if(m.getAllowdel() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getAllowdel().toString());
				m.setAllowdelName(c.getCodename());
			}
			if(m.getNeedorg() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getNeedorg().toString());
				m.setNeedorgName(c.getCodename());
			}
			if(m.getIsactive() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getIsactive().toString());
				m.setIsactiveName(c.getCodename());
			}
			if(m.getIslocal() != null) {
				Map<String, SysCode> publicYN = sysCodeService.getCodeMap("public.YN");
				SysCode c = publicYN.get(m.getIslocal().toString());
				m.setIslocalName(c.getCodename());
			}
			return m;
		}
		return null;
	}
	
	public SysSubsystem doSave(SysSubsystem m) {
		return sysSubsystemRepository.save(m);
	}
	
	public void doDelete(Long id) {
		sysSubsystemRepository.deleteById(id);
	}
	
	public List<SysSubsystem> findAll(){
		return sysSubsystemRepository.findAll();
	}
	
	public List<SysSubsystem> findMySubsystem(Long userid){
		return sysSubsystemRepository.findMySubsystem(userid);
	}
}
