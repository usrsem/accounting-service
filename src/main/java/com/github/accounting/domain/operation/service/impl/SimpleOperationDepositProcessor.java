package com.github.accounting.domain.operation.service.impl;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.dal.repository.AccountRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationDepositRequestModel;
import com.github.accounting.domain.operation.service.OperationDepositProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
public class SimpleOperationDepositProcessor implements OperationDepositProcessor {

    private final AccountRepository accountRepository;

    private final static BiConsumer<AccountDataSource, SynchronousSink<AccountDataSource>> createHandler = (account, sink) -> {
        if (account.getSum() <= 0) {
            sink.error(new RuntimeException("Can't use account with sum less or equals to zero"));
        } else {
            sink.next(account);
        }
    };

    private final static BiConsumer<AccountDataSource, SynchronousSink<AccountDataSource>> rollbackHandler =
            (account, sink) -> sink.next(account);


    @Override
    @Transactional
    public Mono<Void> createDeposit(OperationDepositRequestModel requestModel) {
        return Mono.just(requestModel)
                .flatMap(request -> processTransaction(request, DepositType.CREATE))
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> rollbackDeposit(OperationDepositRequestModel requestModel) {
        return Mono.just(requestModel.swapAccounts())
                .flatMap(request -> processTransaction(request, DepositType.ROLLBACK))
                .then();
    }


    private Mono<OperationDepositRequestModel> processTransaction(OperationDepositRequestModel data, DepositType type) {
        return Mono.just(data)
                .flatMap(d -> subtractSumFromAccount(data, type))
                .flatMap(this::addSumToAccount);
    }

    private Mono<OperationDepositRequestModel> subtractSumFromAccount(
            OperationDepositRequestModel data,
            DepositType type
    ) {
        return Mono.just(data.fromAccountId())
                .flatMap(accountRepository::findById)
                .handle(DepositType.CREATE.equals(type) ? createHandler : rollbackHandler)
                .map(ds -> {
                    ds.setSum(ds.getSum() - data.sum());
                    return ds;
                })
                .flatMap(accountRepository::save)
                .then(Mono.just(data));
    }

    private Mono<OperationDepositRequestModel> addSumToAccount(OperationDepositRequestModel data) {
        return Mono.just(data.toAccountId())
                .flatMap(accountRepository::findById)
                .map(ds -> {
                    ds.setSum(ds.getSum() + data.sum());
                    return ds;
                })
                .flatMap(accountRepository::save)
                .then(Mono.just(data));
    }

    private enum DepositType {
        CREATE,
        ROLLBACK
    }
}
