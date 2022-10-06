package com.github.accounting.domain.operation.model.boundary.output;

import lombok.Builder;
import org.springframework.lang.Nullable;

@Builder
public record AccountDsReadResponseModel(
        Long accountId,
        String name,
        Boolean common,
        @Nullable Long employeeId
) {
}
