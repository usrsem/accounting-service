package com.github.accounting.domain.employee.model.boundary.input;

import com.github.accounting.model.UserData;

public record EmployeeFacadeReadRequestModel(
        UserData userData
) {
}
