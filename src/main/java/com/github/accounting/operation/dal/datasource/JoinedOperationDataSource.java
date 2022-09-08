package com.github.accounting.operation.dal.datasource;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record JoinedOperationDataSource(
        long operationId,
        double sum,
        String description,
        String categoryName,
        String fromAccountName,
        String toAccountName,
        LocalDateTime createdAt
) {
}
