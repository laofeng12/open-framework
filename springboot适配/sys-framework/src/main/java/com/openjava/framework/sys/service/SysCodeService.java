package com.openjava.framework.sys.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.framework.sys.domain.SysCode;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface SysCodeService {
	
	public SysCode get(Integer id);
	
	public List<SysCode> findByCodetype(String codetype);
	
	public List<SysCode> findByCodeAndType(String codeDef,String codetype);
	public SysCode getByCodeAndType(String codeDef,String codetype);
	
	public Map<String, SysCode> getCodeMap(String codetype);
}
