package com.github.accounting.domain.account.model.boundary.input;

public record AccountCreateRequestModel(
        String name,
        boolean common,
        Long employeeId
) {
}
