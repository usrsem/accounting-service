package com.github.accounting.operation.model.mapper;

import com.github.accounting.operation.dal.datasource.OperationDSReadResponseModel;
import com.github.accounting.operation.dal.datasource.OperationDataSource;
import com.github.accounting.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import org.springframework.stereotype.Component;

@Component
public class OperationMapper {
    public OperationReadResponseModel dSReadResponseModel2ReadResponseModel(OperationDSReadResponseModel ds) {
        return OperationReadResponseModel.builder()
                .id(ds.getOperationId())
                .sum(ds.getSum())
                .description(ds.getDescription())
                .categoryName(ds.getCategoryName())
                .fromAccountName(ds.getFromAccountName())
                .toAccountName(ds.getToAccountName())
                .createdAt(ds.getCreatedAt())
                .build();
    }

    public OperationDataSource createRequestModel2DataSource(OperationCreateRequestModel requestModel) {
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
