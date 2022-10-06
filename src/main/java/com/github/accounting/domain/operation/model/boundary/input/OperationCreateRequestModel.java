package com.github.accounting.domain.operation.model.boundary.input;

import com.github.accounting.model.EmployeeAuthority;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationCreateRequestModel {
    private Integer categoryId;
    private Long fromAccountId;
    private Long toAccountId;
    private Long employeeId;
    private String description;
    private Float sum;
    private EmployeeAuthority role;
}
