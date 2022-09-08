package com.github.accounting.operation.model.boundary.input;

import com.github.accounting.model.UserData;

public record OperationFacadeReadRequestModel(
        OperationReadRequestModel requestModel,
        UserData userData
) {}
