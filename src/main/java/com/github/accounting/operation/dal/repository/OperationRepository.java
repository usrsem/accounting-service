package com.github.accounting.operation.dal.repository;

import com.github.accounting.operation.dal.datasource.OperationDataSource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OperationRepository extends ReactiveCrudRepository<OperationDataSource, Long>,
        CustomOperationRepository {

}
