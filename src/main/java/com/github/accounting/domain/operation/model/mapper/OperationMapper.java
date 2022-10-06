package com.github.accounting.domain.operation.model.mapper;

import com.github.accounting.domain.operation.dal.datasource.OperationDataSource;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDepositRequestModel;
import com.github.accounting.domain.operation.model.boundary.output.OperationReadResponseModel;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {
    public OperationReadResponseModel dsReadResponseModel2ReadResponseModel(OperationDSReadResponseModel ds) {
        return OperationReadResponseModel.builder()
                .id(ds.getOperationId())
                .sum(ds.getSum())
                .description(ds.getDescription())
                .categoryName(ds.getCategoryName())
                .fromAccountName(ds.getFromAccount().name())
                .toAccountName(ds.getToAccount().name())
                .createdAt(ds.getCreatedAt())
                .status(ds.getStatus())
                .build();
    }

    public OperationDataSource createRequestModel2ds(OperationCreateRequestModel requestModel) {
        return OperationDataSource.builder()
                .categoryId(requestModel.getCategoryId())
                .fromAccountId(requestModel.getFromAccountId())
                .toAccountId(requestModel.getToAccountId())
                .employeeId(requestModel.getEmployeeId())
                .description(requestModel.getDescription())
                .sum(requestModel.getSum())
                .build();
    }

    public OperationDepositRequestModel create2Deposit(OperationCreateRequestModel requestModel) {
        return OperationDepositRequestModel.builder()
                .fromAccountId(requestModel.getFromAccountId())
                .toAccountId(requestModel.getToAccountId())
                .sum(requestModel.getSum())
                .build();
    }

    public OperationDepositRequestModel ds2Deposit(OperationDataSource ds) {
        return OperationDepositRequestModel.builder()
                .fromAccountId(ds.getFromAccountId())
                .toAccountId(ds.getToAccountId())
                .sum(ds.getSum())
                .build();
    }
}
