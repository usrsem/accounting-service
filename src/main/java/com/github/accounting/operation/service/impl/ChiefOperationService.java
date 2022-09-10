package com.github.accounting.operation.service.impl;

import com.github.accounting.operation.dal.datasource.OperationDSReadResponseModel;
import com.github.accounting.operation.dal.repository.OperationRepository;
import com.github.accounting.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationReadRequestModel;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import com.github.accounting.operation.model.mapper.OperationMapper;
import com.github.accounting.operation.service.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Service
@AllArgsConstructor
public class ChiefOperationService implements OperationService {

    private final OperationRepository repository;
    private final OperationMapper mapper;

    @Override
    public Flux<OperationReadResponseModel> readOperations(OperationReadRequestModel requestModel) {
        return repository
                .findAllWithDetails()
                .filter(createOperationsFilter(requestModel.employeeId()))
                .map(createSumSignsAdder(requestModel.employeeId()))
                .map(mapper::dSReadResponseModel2ReadResponseModel);
    }

    private Predicate<OperationDSReadResponseModel> createOperationsFilter(long employeeId) {
        return  ds -> ds.getEmployeeId() == employeeId
                        || ds.getToEmployeeId() == employeeId
                        || ds.getEmployeeAuthority().equals("WORKER");
    }

    private UnaryOperator<OperationDSReadResponseModel> createSumSignsAdder(long employeeId) {
        return ds -> {
            ds.setSum(ds.getEmployeeId() == employeeId ? -ds.getSum() : ds.getSum());
            return ds;
        };
    }


    @Override
    @Transactional
    public Mono<Void> createOperation(OperationCreateRequestModel requestModel) {
        // TODO: Validate request
        // TODO: fromAccount should be null
        // TODO: toAccount should be a WORKER
        return repository
                .save(mapper.createRequestModel2DataSource(requestModel))
                .then();
    }


    @Override
    @Transactional
    public Mono<Void> deleteOperation(OperationDeleteRequestModel requestModel) {
        // TODO: Check date before deleting
        return repository
                .deleteById(requestModel.id())
                .then();
    }
}
