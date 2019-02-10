package com.openjava.example.file.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.example.file.domain.ExampleUploadFile;
import com.openjava.example.file.query.ExampleUploadFileDBParam;

/**
 * 业务层接口
 * @author hzy
 *
 */
public interface ExampleUploadFileService {
	public Page<ExampleUploadFile> query(ExampleUploadFileDBParam params, Pageable pageable);
	
	public List<ExampleUploadFile> queryDataOnly(ExampleUploadFileDBParam params, Pageable pageable);
	
	public ExampleUploadFile get(Long id);
	
	public ExampleUploadFile doSave(ExampleUploadFile m);
	
	public void doDelete(Long id);
	public void doRemove(String ids);
}
