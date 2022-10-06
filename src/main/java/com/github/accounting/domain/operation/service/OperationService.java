package com.github.accounting.domain.operation.service;

import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationReadRequestModel;
import com.github.accounting.domain.operation.model.boundary.output.OperationReadResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OperationService {
    Flux<OperationReadResponseModel> readOperations(OperationReadRequestModel requestModel);

    Mono<Void> createOperation(OperationCreateRequestModel requestModel);

    Mono<Void> deleteOperation(OperationDeleteRequestModel requestModel);
}
