package com.openjava.admin.logs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.logs.domain.SysLogApiError;
import com.openjava.admin.logs.query.LogApiErrorDBParam;

/**
 * 异常日志业务层接口
 * @author heziyou
 *
 */
public interface LogApiErrorService {
	Page<SysLogApiError> query(LogApiErrorDBParam params, Pageable pageable);
	
	List<SysLogApiError> queryDataOnly(LogApiErrorDBParam params, Pageable pageable);
	
	SysLogApiError get(String id);
	
	
}
