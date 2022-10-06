package com.github.accounting.domain.operation.model.boundary.input;

import com.github.accounting.model.EmployeeAuthority;

public record OperationDeleteRequestModel(long id, EmployeeAuthority role) {
}
