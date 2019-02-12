package com.openjava.admin.logs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.admin.logs.domain.SysLogApiRequest;

/**
 * 请求日志业务层接口
 * @author 子右
 *
 */
public interface LogApiRequestService {
	
	SysLogApiRequest get(Long id);
	
	
}
