package com.github.accounting.account.controller;

import com.github.accounting.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.account.service.AccountService;
import com.github.accounting.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController("/api/v1/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService service;
    private final UserData userData = new UserData(1, "Andrey", List.of("ROLE_CHIEF"));

    @GetMapping
    public Flux<AccountReadResponseModel> read() {
        var requestModel = new AccountReadRequestModel(userData);
        return service.readAccounts(requestModel);
    }

    @PostMapping
    public Mono<Void> create(@RequestBody AccountCreateRequestModel requestModel) {
        return service.createAccount(requestModel);
    }
}
