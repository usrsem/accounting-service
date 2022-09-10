package com.github.accounting.operation.dal.repository;

import com.github.accounting.operation.dal.datasource.OperationDSReadResponseModel;
import reactor.core.publisher.Flux;

public interface CustomOperationRepository {
    Flux<OperationDSReadResponseModel> findAllWithDetails();
}
