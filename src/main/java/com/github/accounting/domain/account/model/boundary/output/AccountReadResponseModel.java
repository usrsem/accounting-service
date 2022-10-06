package com.github.accounting.domain.account.model.boundary.output;

import lombok.Builder;

@Builder
public record AccountReadResponseModel(
        long id,
        String name,
        boolean common,
        double sum
) {
}
