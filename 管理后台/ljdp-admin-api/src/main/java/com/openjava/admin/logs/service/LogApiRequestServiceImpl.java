package com.openjava.admin.logs.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.admin.logs.domain.SysLogApiRequest;
import com.openjava.admin.logs.repository.LogApiRequestRepository;
/**
 * 请求日志业务层
 * @author 子右
 *
 */
@Service
@Transactional
public class LogApiRequestServiceImpl implements LogApiRequestService {
	
	@Resource
	private LogApiRequestRepository logApiRequestRepository;
	
	
	public SysLogApiRequest get(Long id) {
		Optional<SysLogApiRequest> o = logApiRequestRepository.findById(id);
		if(o.isPresent()) {
			SysLogApiRequest m = o.get();
			return m;
		}
		System.out.println("找不到记录LogApiRequest："+id);
		return null;
	}
	
	
}
