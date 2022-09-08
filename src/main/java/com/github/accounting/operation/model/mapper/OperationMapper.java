package com.github.accounting.operation.model.mapper;

import com.github.accounting.operation.dal.datasource.JoinedOperationDataSource;
import com.github.accounting.operation.dal.datasource.OperationDataSource;
import com.github.accounting.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {
    public OperationReadResponseModel map(JoinedOperationDataSource ds) {
        return OperationReadResponseModel.builder()
                .id(ds.operationId())
                .sum(ds.sum())
                .description(ds.description())
                .categoryName(ds.categoryName())
                .fromAccountName(ds.fromAccountName())
                .toAccountName(ds.toAccountName())
                .createdAt(ds.createdAt())
                .build();
    }

    public OperationDataSource map(OperationCreateRequestModel requestModel) {
        return OperationDataSource.builder()
                .categoryId(requestModel.getCategoryId())
                .fromAccountId(requestModel.getFromAccountId())
                .toAccountId(requestModel.getToAccountId())
                .employeeId(requestModel.getEmployeeId())
                .description(requestModel.getDescription())
                .sum(requestModel.getSum())
                .build();
    }
}
