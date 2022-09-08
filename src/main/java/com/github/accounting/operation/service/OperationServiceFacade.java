package com.github.accounting.operation.service;

import com.github.accounting.operation.model.boundary.input.OperationFacadeCreateRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationFacadeDeleteRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationFacadeReadRequestModel;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OperationServiceFacade {
    Flux<OperationReadResponseModel> readOperations(OperationFacadeReadRequestModel requestModel);
    Mono<Void> createOperation(OperationFacadeCreateRequestModel requestModel);
    Mono<Void> deleteOperation(OperationFacadeDeleteRequestModel requestModel);
}
