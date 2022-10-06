package com.github.accounting.domain.operation.service.impl;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationDepositRequestModel;
import com.github.accounting.domain.operation.service.OperationDepositProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class SimpleOperationDepositProcessorTest {

    @Autowired
    OperationDepositProcessor processor;

    @MockBean
    AccountRepository accountRepository;

    private final static OperationDepositRequestModel requestModel = OperationDepositRequestModel.builder()
            .fromAccountId(1L)
            .toAccountId(2L)
            .sum(50f)
            .build();

    private final static long fromAccountId = requestModel.fromAccountId();
    private final static long toAccountId = requestModel.toAccountId();

    private AccountDataSource createFromAccountDs() {
        return AccountDataSource.builder()
                .id(fromAccountId)
                .name("fromAccount")
                .sum(0d)
                .build();
    }

    private AccountDataSource createToAccountDs() {
        return AccountDataSource.builder()
                .id(toAccountId)
                .name("toAccount")
                .sum(0d)
                .build();
    }

    // TODO: 28.09.2022 Throw on less then sum 
    @Test
    void createDeposit_throws_on_less_then_1_sum_on_fromAccount() {
        when(accountRepository.findById(fromAccountId)).thenReturn(Mono.just(createFromAccountDs()));

        StepVerifier
                .create(processor.createDeposit(requestModel))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Can't use account with sum less or equals to zero"))
                .verify();

        verify(accountRepository).findById(fromAccountId);
    }

    // TODO: 28.09.2022 Return response with message about sum less then 0
    @Test
    void rollbackDeposit_not_throws_on_less_then_1_sum_on_toAccount() {
        when(accountRepository.findById(fromAccountId)).thenReturn(Mono.just(createFromAccountDs()));
        when(accountRepository.findById(toAccountId)).thenReturn(Mono.just(createToAccountDs()));
        when(accountRepository.save(any())).thenReturn(Mono.empty());

        StepVerifier
                .create(processor.rollbackDeposit(requestModel))
                .verifyComplete();

        verify(accountRepository).findById(argThat((Long arg) -> arg.equals(toAccountId)));
        verify(accountRepository).findById(argThat((Long arg) -> arg.equals(fromAccountId)));
        verify(accountRepository, times(2)).findById(anyLong());
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void createDeposit_adds_sum_to_toAccount_and_subtracts_sum_from_fromAccount() {
        var fromAccountDs = createFromAccountDs();
        var toAccountDs = createToAccountDs();

        fromAccountDs.setSum(100d);

        when(accountRepository.save(any())).thenReturn(Mono.empty());
        when(accountRepository.findById(fromAccountId)).thenReturn(Mono.just(fromAccountDs));
        when(accountRepository.findById(toAccountId)).thenReturn(Mono.just(toAccountDs));

        StepVerifier
                .create(processor.createDeposit(requestModel))
                .verifyComplete();

        fromAccountDs.setSum(toAccountDs.getSum() - requestModel.sum());
        toAccountDs.setSum(toAccountDs.getSum() + requestModel.sum());

        verify(accountRepository).save(argThat(arg -> arg.getId() == fromAccountId
                && fromAccountDs.getSum().equals(arg.getSum())));
        verify(accountRepository).save(argThat(arg -> arg.getId() == toAccountId
                && toAccountDs.getSum().equals(arg.getSum())));
        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void rollbackDeposit_adds_sum_to_fromAccount_and_subtracts_sum_from_toAccount() {
        var fromAccountDs = createFromAccountDs();
        var toAccountDs = createToAccountDs();

        fromAccountDs.setSum(100d);

        when(accountRepository.save(any())).thenReturn(Mono.empty());
        when(accountRepository.findById(fromAccountId)).thenReturn(Mono.just(fromAccountDs));
        when(accountRepository.findById(toAccountId)).thenReturn(Mono.just(toAccountDs));

        StepVerifier
                .create(processor.createDeposit(requestModel))
                .verifyComplete();

        fromAccountDs.setSum(toAccountDs.getSum() - requestModel.sum());
        toAccountDs.setSum(toAccountDs.getSum() + requestModel.sum());

        verify(accountRepository).save(argThat(arg -> arg.getId() == fromAccountId
                && fromAccountDs.getSum().equals(arg.getSum())));
        verify(accountRepository).save(argThat(arg -> arg.getId() == toAccountId
                && toAccountDs.getSum().equals(arg.getSum())));
        verify(accountRepository, times(2)).save(any());
    }
}