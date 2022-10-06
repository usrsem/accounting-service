package com.github.accounting.domain.employee.model.boundary.input;

import com.github.accounting.model.UserData;

public record EmployeeFacadeReadCurrentRequestModel(
        EmployeeReadCurrentRequestModel request,
        UserData userData
) {
}
