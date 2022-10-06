package com.github.accounting.domain.operation.service.impl.preprocessor;

import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class BaseOperationPreprocessor implements OperationPreprocessor {

    @Override
    public Mono<OperationCreateRequestModel> preprocessCreation(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(this::checkSumMoreThenZero)
                .flatMap(this::checkAccountsDifference);
    }

    private Mono<OperationCreateRequestModel> checkSumMoreThenZero(OperationCreateRequestModel requestModel) {
        if (requestModel.getSum() <= 0) {
            return Mono.error(new RuntimeException("Sum should be more then 0"));
        }
        return Mono.just(requestModel);
    }

    private Mono<OperationCreateRequestModel> checkAccountsDifference(OperationCreateRequestModel requestModel) {
        if (Objects.equals(requestModel.getToAccountId(), requestModel.getFromAccountId())) {
            return Mono.error(new RuntimeException("Account should be different"));
        }
        return Mono.just(requestModel);
    }


    @Override
    public Mono<OperationDeleteRequestModel> preprocessDeletion(OperationDeleteRequestModel requestModel) {
        return Mono.just(requestModel);
    }
}
