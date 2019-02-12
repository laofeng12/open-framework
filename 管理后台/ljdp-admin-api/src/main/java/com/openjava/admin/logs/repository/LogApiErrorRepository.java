package com.openjava.admin.logs.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.admin.logs.domain.SysLogApiError;

/**
 * 异常日志数据库访问层
 * @author heziyou
 *
 */
public interface LogApiErrorRepository extends DynamicJpaRepository<SysLogApiError, String>, LogApiErrorRepositoryCustom{
	
}
