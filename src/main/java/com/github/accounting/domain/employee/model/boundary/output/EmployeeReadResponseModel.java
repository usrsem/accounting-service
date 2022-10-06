package com.github.accounting.domain.employee.model.boundary.output;

import lombok.Builder;

@Builder
public record EmployeeReadResponseModel(
        Long id,
        String fullname,
        String authority
) {
}
