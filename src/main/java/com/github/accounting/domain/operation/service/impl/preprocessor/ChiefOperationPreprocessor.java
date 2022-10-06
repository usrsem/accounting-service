package com.github.accounting.domain.operation.service.impl.preprocessor;

import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.domain.operation.dal.repository.OperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ChiefOperationPreprocessor extends BaseOperationPreprocessor implements OperationPreprocessor {

    private final EmployeeRepository employeeRepository;
    private final OperationRepository operationRepository;

    @Override
    public Mono<OperationCreateRequestModel> preprocessCreation(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(super::preprocessCreation)
                .flatMap(this::verifyToAccountAuthority);
    }

    @Override
    public Mono<OperationDeleteRequestModel> preprocessDeletion(OperationDeleteRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(super::preprocessDeletion)
                .map(OperationDeleteRequestModel::id)
                .flatMap(operationRepository::findById)
                .handle((operation, sink) -> {
                    var duration = Duration.between(operation.getCreatedAd(), LocalDateTime.now());
                    if (!duration.minusHours(24).isNegative()) {
                        sink.error(new RuntimeException("You can delete operation only in 24h"));
                    }
                })
                .then(Mono.just(requestModel));

    }


    private Mono<OperationCreateRequestModel> verifyToAccountAuthority(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel.getToAccountId())
                .flatMap(employeeRepository::findByAccountId)
                .handle((employee, sink) -> {
                    if (!"WORKER".equals(employee.getAuthority())) {
                        sink.error(new RuntimeException("To account should be a worker account"));
                    }
                })
                .then(Mono.just(requestModel));
    }
}
