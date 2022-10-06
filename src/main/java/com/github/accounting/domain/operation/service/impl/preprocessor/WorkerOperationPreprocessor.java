package com.github.accounting.domain.operation.service.impl.preprocessor;

import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.operation.dal.repository.OperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@AllArgsConstructor
public class WorkerOperationPreprocessor extends BaseOperationPreprocessor implements OperationPreprocessor {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Override
    public Mono<OperationCreateRequestModel> preprocessCreation(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(super::preprocessCreation)
                .flatMap(this::validateFromAccount);
    }

    @Override
    public Mono<OperationDeleteRequestModel> preprocessDeletion(OperationDeleteRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(super::preprocessDeletion)
                .flatMap(this::checkCreatedAtOfOperation);
    }


    private Mono<OperationCreateRequestModel> validateFromAccount(OperationCreateRequestModel requestModel) {
        return accountRepository.findById(requestModel.getFromAccountId())
                .handle((account, sink) -> {
                    if (!Objects.equals(account.getEmployeeId(), requestModel.getEmployeeId())
                            || account.getCommon()) {
                        sink.error(new RuntimeException("Can't add operation"));
                    }
                })
                .then(Mono.just(requestModel));
    }

    private Mono<OperationDeleteRequestModel> checkCreatedAtOfOperation(OperationDeleteRequestModel requestModel) {
        return operationRepository
                .findById(requestModel.id())
                .handle((operation, sink) -> {
                    var duration = Duration.between(operation.getCreatedAd(), LocalDateTime.now());
                    if (!duration.minusHours(24).isNegative()) {
                        sink.error(new RuntimeException("You can delete operation only in 24h"));
                    }
                })
                .then(Mono.just(requestModel));
    }
}
