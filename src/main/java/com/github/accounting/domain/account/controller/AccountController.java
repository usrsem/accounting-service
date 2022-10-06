package com.github.accounting.domain.account.controller;

import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.domain.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.domain.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.domain.account.service.AccountService;
import com.github.accounting.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService serviceFacade;
    private final UserData userData = new UserData(1, "Andrey", List.of("ROLE_CHIEF"));

    @GetMapping
    public Flux<AccountReadResponseModel> read() {
        var request = new AccountReadRequestModel(userData.id());
        return serviceFacade.readAccounts(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(@RequestBody AccountCreateRequestModel request) {
        return serviceFacade.createAccount(request);
    }
}
