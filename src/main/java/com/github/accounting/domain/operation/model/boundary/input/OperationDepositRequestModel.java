package com.github.accounting.domain.operation.model.boundary.input;

import lombok.Builder;

@Builder
public record OperationDepositRequestModel(Long fromAccountId, Long toAccountId, Float sum) {
    public OperationDepositRequestModel swapAccounts() {
        return new OperationDepositRequestModel(
                this.toAccountId,
                this.fromAccountId,
                this.sum
        );
    }
}
