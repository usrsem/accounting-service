package com.github.accounting.domain.account.service.impl;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.domain.account.model.boundary.input.AccountReadRequestModel;
import com.github.accounting.domain.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommonAccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    private final static List<AccountDataSource> accounts = Stream
            .iterate(0L, i -> i + 1L)
            .limit(10)
            .map(i -> AccountDataSource.builder()
                    .id(i)
                    .name("Account#" + i)
                    .employeeId(i % 2 == 0 ? null : i)
                    .common(i % 2 == 0)
                    .sum(100d * i)
                    .build())
            .toList();


    @Test
    void readAccounts_returns_accounts_list() {
        when(accountRepository.findAllRelated(anyLong()))
                .thenReturn(Flux.fromIterable(accounts));

        var request = new AccountReadRequestModel(1L);

        StepVerifier
                .create(accountService.readAccounts(request))
                .expectNextCount(accounts.size())
                .verifyComplete();

        verify(accountRepository).findAllRelated(anyLong());
    }

    @Test
    void readAccounts_not_fails_on_empty_accounts_list() {
        when(accountRepository.findAllRelated(anyLong()))
                .thenReturn(Flux.empty());

        var request = new AccountReadRequestModel(1L);

        StepVerifier
                .create(accountService.readAccounts(request))
                .verifyComplete();

        verify(accountRepository).findAllRelated(anyLong());
    }

    @Test
    void createAccount_creates_new_account() {
        when(accountRepository.save(any()))
                .thenReturn(Mono.empty());

        when(accountRepository.findByEmployeeId(anyLong()))
                .thenReturn(Mono.empty());

        var request = new AccountCreateRequestModel(
                "test name",
                false,
                1L
        );

        StepVerifier
                .create(accountService.createAccount(request))
                .verifyComplete();

        verify(accountRepository).save(any());
        verify(accountRepository).findByEmployeeId(anyLong());
    }

    @Test
    void createAccount_fails_on_creating_existed_account() {
        when(accountRepository.save(any()))
                .thenReturn(Mono.empty());

        when(accountRepository.findByEmployeeId(anyLong()))
                .thenReturn(Mono.just(AccountDataSource.builder().build()));

        var request = new AccountCreateRequestModel(
                "test name",
                false,
                1L
        );

        StepVerifier
                .create(accountService.createAccount(request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && "Account for this employee already exists".equals(throwable.getMessage()))
                .verify();

        verify(accountRepository, times(0)).save(any());
        verify(accountRepository).findByEmployeeId(anyLong());
    }

}