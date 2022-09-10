package com.github.accounting.operation.dal.datasource;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OperationDSReadResponseModel {
    private Long operationId;
    private Long employeeId;
    private Long toEmployeeId;
    private String employeeAuthority;
    private Double sum;
    private String description;
    private String categoryName;
    private String fromAccountName;
    private String toAccountName;
    private LocalDateTime createdAt;
}
