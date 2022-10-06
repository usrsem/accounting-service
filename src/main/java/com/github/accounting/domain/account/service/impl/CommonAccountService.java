package com.github.accounting.domain.account.service.impl;

import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.domain.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.domain.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.domain.account.model.mapper.AccountMapper;
import com.github.accounting.domain.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CommonAccountService implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper mapper;

    @Override
    public Flux<AccountReadResponseModel> readAccounts(AccountReadRequestModel requestModel) {
        return Flux.just(requestModel.employeeId())
                .flatMap(accountRepository::findAllRelated)
                .map(mapper::ds2ReadResponseModel);
    }

    @Override
    public Mono<Void> createAccount(AccountCreateRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(this::verifyUniqueAccountForEmployee)
                .map(mapper::createRequestModel2ds)
                .flatMap(accountRepository::save)
                .then();
    }

    private Mono<AccountCreateRequestModel> verifyUniqueAccountForEmployee(AccountCreateRequestModel requestModel) {
        var requestMono = Mono.just(requestModel);

        if (requestModel.employeeId() == null) {
            return requestMono;
        }

        return Mono.just(requestModel.employeeId())
                .flatMap(accountRepository::findByEmployeeId)
                .handle((ds, sink) -> sink.error(new RuntimeException("Account for this employee already exists")))
                .then(requestMono);
    }
}
