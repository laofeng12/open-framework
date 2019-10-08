package com.openjava.nhc.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openjava.nhc.test.domain.ExampleOrder;

public interface TestOrderRepository extends JpaRepository<ExampleOrder, String>{

}
