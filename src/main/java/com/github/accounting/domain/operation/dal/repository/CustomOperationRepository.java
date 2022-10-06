package com.github.accounting.domain.operation.dal.repository;

import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.model.OperationStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomOperationRepository {
    Flux<OperationDSReadResponseModel> findAllRelatedOperationsWithDetails(Long employeeId);

    Mono<Void> updateOperationStatus(Long operationId, OperationStatus status);
}
