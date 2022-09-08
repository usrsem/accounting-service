package com.github.accounting.operation.model.boundary.input;

import com.github.accounting.model.UserData;

public record OperationFacadeDeleteRequestModel(
        OperationDeleteRequestModel requestModel,
        UserData userData
) {
}
