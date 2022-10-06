package com.github.accounting.domain.employee.dal.repository;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import reactor.core.publisher.Mono;

public interface CustomEmployeeRepository {
    Mono<EmployeeDataSource> findByAccountId(long accountId);
}
