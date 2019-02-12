package com.openjava.admin.logs.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.logs.domain.SysLogApiError;
import com.openjava.admin.logs.query.LogApiErrorDBParam;
import com.openjava.admin.logs.repository.LogApiErrorRepository;
/**
 * 异常日志业务层
 * @author heziyou
 *
 */
@Service
@Transactional
public class LogApiErrorServiceImpl implements LogApiErrorService {
	
	@Resource
	private LogApiErrorRepository logApiErrorRepository;
	
	public Page<SysLogApiError> query(LogApiErrorDBParam params, Pageable pageable){
		Page<SysLogApiError> pageresult = logApiErrorRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<SysLogApiError> queryDataOnly(LogApiErrorDBParam params, Pageable pageable){
		return logApiErrorRepository.queryDataOnly(params, pageable);
	}
	
	public SysLogApiError get(String id) {
		Optional<SysLogApiError> o = logApiErrorRepository.findById(id);
		if(o.isPresent()) {
			SysLogApiError m = o.get();
			return m;
		}
		System.out.println("找不到记录LogApiError："+id);
		return null;
	}
	
	
}
