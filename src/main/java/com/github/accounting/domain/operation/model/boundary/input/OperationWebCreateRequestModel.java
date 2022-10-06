package com.github.accounting.domain.operation.model.boundary.input;

import lombok.Data;

@Data
public class OperationWebCreateRequestModel {
    private Integer categoryId;
    private Long fromAccountId;
    private Long toAccountId;
    private String description;
    private Float sum;
}
