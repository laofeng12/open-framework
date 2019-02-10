package com.openjava.framework.sys.service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.repository.SysCodeRepository;

/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class SysCodeServiceImpl implements SysCodeService {
	
	@Resource
	private SysCodeRepository sysCodeRepository;
	
	
	public SysCode get(Integer id) {
		Optional<SysCode> o = sysCodeRepository.findById(id);
		if(o.isPresent()) {
			return o.get();
		}
		return null;
	}
	
	public List<SysCode> findByCodetype(String codetype){
		Sort sort = Sort.by(Sort.Order.asc("sort"));
		return sysCodeRepository.findByCodetype(codetype, sort);
	}
	
	public List<SysCode> findByCodeAndType(String codeDef,String codetype){
		return sysCodeRepository.findByCodeAndType(codeDef, codetype);
	}
	
	public SysCode getByCodeAndType(String codeDef,String codetype){
		List<SysCode> list = sysCodeRepository.findByCodeAndType(codeDef, codetype);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public Map<String, SysCode> getCodeMap(String codetype){
		Map<String, SysCode> map = new HashMap<>();
		List<SysCode> list = sysCodeRepository.findByCodetype(codetype);
		for (SysCode c : list) {
			map.put(c.getCodeDef(), c);
		}
		return map;
	}
}
