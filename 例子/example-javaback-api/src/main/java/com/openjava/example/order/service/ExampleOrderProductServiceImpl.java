package com.openjava.example.order.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.example.order.domain.ExampleOrderProduct;
import com.openjava.example.order.query.ExampleOrderProductDBParam;
import com.openjava.example.order.repository.ExampleOrderProductRepository;
/**
 * 业务层
 * @author hzy
 *
 */
@Service
@Transactional
public class ExampleOrderProductServiceImpl implements ExampleOrderProductService {
	
	@Resource
	private ExampleOrderProductRepository exampleOrderProductRepository;
	
	public Page<ExampleOrderProduct> query(ExampleOrderProductDBParam params, Pageable pageable){
		Page<ExampleOrderProduct> pageresult = exampleOrderProductRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<ExampleOrderProduct> queryDataOnly(ExampleOrderProductDBParam params, Pageable pageable){
		return exampleOrderProductRepository.queryDataOnly(params, pageable);
	}
	
	public ExampleOrderProduct get(String id) {
		Optional<ExampleOrderProduct> m = exampleOrderProductRepository.findById(id);
		return m.get();
	}
	
	public ExampleOrderProduct doSave(ExampleOrderProduct m) {
		return exampleOrderProductRepository.save(m);
	}
	
	public void doDelete(String id) {
		exampleOrderProductRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			exampleOrderProductRepository.deleteById(new String(items[i]));
		}
	}
}
