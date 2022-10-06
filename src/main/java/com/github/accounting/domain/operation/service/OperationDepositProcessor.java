package com.github.accounting.domain.operation.service;

import com.github.accounting.domain.operation.model.boundary.input.OperationDepositRequestModel;
import reactor.core.publisher.Mono;

public interface OperationDepositProcessor {
    Mono<Void> createDeposit(OperationDepositRequestModel requestModel);

    Mono<Void> rollbackDeposit(OperationDepositRequestModel requestModel);
}
