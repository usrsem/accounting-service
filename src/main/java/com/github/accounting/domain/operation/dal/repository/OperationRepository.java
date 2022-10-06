package com.github.accounting.domain.operation.dal.repository;

import com.github.accounting.domain.operation.dal.datasource.OperationDataSource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OperationRepository extends ReactiveCrudRepository<OperationDataSource, Long>,
        CustomOperationRepository {
}
