package com.github.accounting.domain.operation.model.boundary.input;

import com.github.accounting.domain.operation.model.boundary.output.AccountDsReadResponseModel;
import com.github.accounting.model.OperationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class OperationDSReadResponseModel {
    private Long operationId;
    private String description;
    private Double sum;
    private AccountDsReadResponseModel fromAccount;
    private AccountDsReadResponseModel toAccount;
    private String categoryName;
    private Long employeeId;
    private LocalDateTime createdAt;
    private OperationStatus status;
}
