package com.github.accounting.operation.dal.mapper;

import com.github.accounting.operation.dal.datasource.OperationDSReadResponseModel;
import io.r2dbc.spi.Row;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Component
public class OperationDataSourceMapper implements BiFunction<Row, Object, OperationDSReadResponseModel> {

    @Override
    @SneakyThrows
    public OperationDSReadResponseModel apply(Row row, Object o) {
        return OperationDSReadResponseModel.builder()
                .operationId(row.get("operation_id", Long.class))
                .employeeId(row.get("employee_id", Long.class))
                .toEmployeeId(row.get("to_employee_id", Long.class))
                .employeeAuthority(row.get("employee_authority", String.class))
                .sum(row.get("operation_sum", Double.class))
                .description(row.get("description", String.class))
                .categoryName(row.get("category_name", String.class))
                .fromAccountName(row.get("from_account_name", String.class))
                .toAccountName(row.get("to_account_name", String.class))
                .createdAt(row.get("operation_created_at", LocalDateTime.class))
                .build();
    }
}
