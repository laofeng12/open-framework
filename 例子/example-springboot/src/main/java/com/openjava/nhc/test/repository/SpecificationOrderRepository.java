package com.openjava.nhc.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.openjava.nhc.test.domain.ExampleOrder;

public interface SpecificationOrderRepository extends JpaRepository<ExampleOrder, String>, JpaSpecificationExecutor<ExampleOrder>{

}
