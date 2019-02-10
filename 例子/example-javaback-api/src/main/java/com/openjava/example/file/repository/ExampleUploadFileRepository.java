package com.openjava.example.file.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.example.file.domain.ExampleUploadFile;

/**
 * 数据库访问层
 * @author hzy
 *
 */
public interface ExampleUploadFileRepository extends DynamicJpaRepository<ExampleUploadFile, Long>, ExampleUploadFileRepositoryCustom{
	/**
	 * 按ID删除
	 */
	@Modifying
	@Query("delete ExampleUploadFile t where t.fid=:fid")
	public int deleteByPkId(@Param("fid")Long fid);
}
