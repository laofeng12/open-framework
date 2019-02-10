package com.openjava.example.file.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.example.file.domain.ExampleUploadFile;
import com.openjava.example.file.query.ExampleUploadFileDBParam;
import com.openjava.example.file.repository.ExampleUploadFileRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class ExampleUploadFileServiceImpl implements ExampleUploadFileService {
	
	@Resource
	private ExampleUploadFileRepository exampleUploadFileRepository;
	
	public Page<ExampleUploadFile> query(ExampleUploadFileDBParam params, Pageable pageable){
		Page<ExampleUploadFile> pageresult = exampleUploadFileRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<ExampleUploadFile> queryDataOnly(ExampleUploadFileDBParam params, Pageable pageable){
		return exampleUploadFileRepository.queryDataOnly(params, pageable);
	}
	
	public ExampleUploadFile get(Long id) {
		Optional<ExampleUploadFile> m = exampleUploadFileRepository.findById(id);
		return m.get();
	}
	
	public ExampleUploadFile doSave(ExampleUploadFile m) {
		return exampleUploadFileRepository.save(m);
	}
	
	public void doDelete(Long id) {
		exampleUploadFileRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			exampleUploadFileRepository.deleteById(new Long(items[i]));
		}
	}
}
