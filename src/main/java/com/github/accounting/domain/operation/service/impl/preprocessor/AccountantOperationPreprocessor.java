package com.github.accounting.domain.operation.service.impl.preprocessor;

import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.category.dal.repository.CategoryRepository;
import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AccountantOperationPreprocessor extends BaseOperationPreprocessor implements OperationPreprocessor {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Mono<OperationCreateRequestModel> preprocessCreation(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(super::preprocessCreation)
                .flatMap(this::validateToCheck)
                .flatMap(this::setFromCheck)
                .flatMap(this::setCategory);
    }

    @Override
    public Mono<OperationDeleteRequestModel> preprocessDeletion(OperationDeleteRequestModel requestModel) {
        return Mono.error(new RuntimeException("Can't delete operations"));
    }


    private Mono<OperationCreateRequestModel> validateToCheck(OperationCreateRequestModel requestModel) {
        return employeeRepository
                .findByAccountId(requestModel.getToAccountId())
                .handle((employee, sink) -> {
                    if (!"CHIEF".equals(employee.getAuthority())) {
                        sink.error(new RuntimeException("Can't add operation"));
                    }
                })
                .then(Mono.just(requestModel));
    }

    private Mono<OperationCreateRequestModel> setFromCheck(OperationCreateRequestModel requestModel) {
        return Mono.just(requestModel.getEmployeeId())
                .flatMap(accountRepository::findByEmployeeId)
                .map(account -> {
                    requestModel.setFromAccountId(account.getId());
                    return requestModel;
                });
    }

    private Mono<OperationCreateRequestModel> setCategory(OperationCreateRequestModel requestModel) {
        return Mono.just("accounting")
                .flatMap(categoryRepository::findByName)
                .map(category -> {
                    requestModel.setCategoryId(category.getId());
                    return requestModel;
                });
    }
}
