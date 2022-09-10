package com.github.accounting.account.model.boundary.output;

import lombok.Builder;

@Builder
public record AccountReadResponseModel(
        long id,
        String name,
        boolean common
) {
}
