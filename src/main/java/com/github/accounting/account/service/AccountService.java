package com.github.accounting.account.service;

import com.github.accounting.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.account.model.boundary.output.AccountReadResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Flux<AccountReadResponseModel> readAccounts(AccountReadRequestModel requestModel);
    Mono<Void> createAccount(AccountCreateRequestModel requestModel);
}
