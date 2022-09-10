package com.github.accounting.account.service.impl;

import com.github.accounting.account.dal.repository.AccountRepository;
import com.github.accounting.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.account.model.mapper.AccountMapper;
import com.github.accounting.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ReactiveAccountService implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public Flux<AccountReadResponseModel> readAccounts(AccountReadRequestModel requestModel) {
        return repository
                .findAll()
                .map(mapper::dS2ReadResponseModel);
    }

    @Override
    public Mono<Void> createAccount(AccountCreateRequestModel requestModel) {
        return repository
                .save(mapper.createRequestModel2Ds(requestModel))
                .then();
    }
}
