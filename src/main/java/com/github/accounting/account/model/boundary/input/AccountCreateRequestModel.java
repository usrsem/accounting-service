package com.github.accounting.account.model.boundary.input;

public record AccountCreateRequestModel(
        String name,
        boolean common,
        Long employeeId
) {
}
