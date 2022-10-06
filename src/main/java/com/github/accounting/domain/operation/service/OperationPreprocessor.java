package com.github.accounting.domain.operation.service;

import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import reactor.core.publisher.Mono;

public interface OperationPreprocessor {
    Mono<OperationCreateRequestModel> preprocessCreation(OperationCreateRequestModel requestModel);

    Mono<OperationDeleteRequestModel> preprocessDeletion(OperationDeleteRequestModel requestModel);
}
