package com.openjava.framework.sys.repository;

import java.util.List;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.framework.sys.domain.LmMemberToken;

/**
 * 数据库访问层
 * @author hzy
 *
 */
//public interface LmMemberTokenRepository extends DynamicJpaRepository<LmMemberToken, String>, LmMemberTokenRepositoryCustom{
//	
//	@Query("from LmMemberToken where passId=:passId and userAgent=:userAgent and state='1'")
//	public List<LmMemberToken> findByPassIdAndAgent(@Param("passId")Long passId, @Param("userAgent")String userAgent);
//}
