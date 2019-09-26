package com.openjava.nhc.mybatis.service;

import org.springframework.data.domain.Pageable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.openjava.nhc.mybatis.domain.ExampleOrderEntity;
import com.openjava.nhc.test.query.ExampleOrderDBParam;

public interface IExampleOrderService extends IService<ExampleOrderEntity>{

	IPage<ExampleOrderEntity> query(ExampleOrderDBParam params, Pageable pageable);
}
