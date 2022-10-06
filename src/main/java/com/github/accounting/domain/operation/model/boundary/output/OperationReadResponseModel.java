package com.github.accounting.domain.operation.model.boundary.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.accounting.model.OperationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OperationReadResponseModel(
        long id,
        double sum,
        String description,
        String categoryName,
        String fromAccountName,
        String toAccountName,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        LocalDateTime createdAt
) {
}
