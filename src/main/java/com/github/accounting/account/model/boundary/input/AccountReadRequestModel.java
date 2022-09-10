package com.github.accounting.account.model.boundary.input;

import com.github.accounting.model.UserData;

public record AccountReadRequestModel(
        UserData userData
) {
}
