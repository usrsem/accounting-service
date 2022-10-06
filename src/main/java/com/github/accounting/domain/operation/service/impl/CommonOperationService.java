package com.github.accounting.domain.operation.service.impl;

import com.github.accounting.domain.operation.dal.repository.OperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationReadRequestModel;
import com.github.accounting.domain.operation.model.boundary.output.OperationReadResponseModel;
import com.github.accounting.domain.operation.model.mapper.OperationMapper;
import com.github.accounting.domain.operation.service.OperationDepositProcessor;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import com.github.accounting.domain.operation.service.OperationService;
import com.github.accounting.model.EmployeeAuthority;
import com.github.accounting.model.OperationStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CommonOperationService implements OperationService {

    private final OperationDepositProcessor depositProcessor;
    private final Map<String, OperationPreprocessor> preprocessors;
    private final OperationRepository operationRepository;
    private final OperationMapper mapper;

    @PostConstruct
    public void checkPreprocessorsContents() {
        for (var role : EmployeeAuthority.values()) {
            getPreprocessorByRole(role);
        }
    }

    @Override
    public Flux<OperationReadResponseModel> readOperations(OperationReadRequestModel requestModel) {
        return Flux.just(requestModel.employeeId())
                .flatMap(operationRepository::findAllRelatedOperationsWithDetails)
                .filter(Objects::nonNull)
                .map(mapper::dsReadResponseModel2ReadResponseModel);
    }

    @Override
    @Transactional
    public Mono<Void> createOperation(OperationCreateRequestModel requestModel) {
        var processor = getPreprocessorByRole(requestModel.getRole());
        return Mono.just(requestModel)
                .flatMap(processor::preprocessCreation)
                .flatMap(this::createDeposit)
                .map(mapper::createRequestModel2ds)
                .flatMap(operationRepository::save)
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> deleteOperation(OperationDeleteRequestModel requestModel) {
        var processor = getPreprocessorByRole(requestModel.role());
        return Mono.just(requestModel)
                .flatMap(processor::preprocessDeletion)
                .flatMap(this::rollbackDeposit)
                .map(OperationDeleteRequestModel::id)
                .flatMap(id -> operationRepository.updateOperationStatus(id, OperationStatus.DELETED));
    }


    private Mono<OperationCreateRequestModel> createDeposit(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .map(mapper::create2Deposit)
                .flatMap(depositProcessor::createDeposit)
                .then(Mono.just(requestModel));
    }

    private Mono<OperationDeleteRequestModel> rollbackDeposit(OperationDeleteRequestModel requestModel) {
        return Mono.just(requestModel.id())
                .flatMap(operationRepository::findById)
                .map(mapper::ds2Deposit)
                .flatMap(depositProcessor::rollbackDeposit)
                .then(Mono.just(requestModel));
    }

    private OperationPreprocessor getPreprocessorByRole(EmployeeAuthority role) {
        var preprocessorName = createPreprocessorNameFromRole(role.name());
        var preprocessor = preprocessors.get(preprocessorName);
        if (preprocessor == null) {
            throw new RuntimeException("Preprocessor not found for role: " + role);
        }
        return preprocessor;
    }

    private String createPreprocessorNameFromRole(String role) {
        return role.substring(0, 1).toLowerCase()
                + role.substring(1).toLowerCase()
                + OperationPreprocessor.class.getSimpleName();
    }
}
