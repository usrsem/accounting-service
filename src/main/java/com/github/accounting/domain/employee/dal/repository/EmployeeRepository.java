package com.github.accounting.domain.employee.dal.repository;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends ReactiveCrudRepository<EmployeeDataSource, Long>, CustomEmployeeRepository {
}
