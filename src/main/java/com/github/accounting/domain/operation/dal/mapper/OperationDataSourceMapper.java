package com.github.accounting.domain.operation.dal.mapper;

import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.domain.operation.model.boundary.output.AccountDsReadResponseModel;
import com.github.accounting.model.OperationStatus;
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

        var fromAccount = AccountDsReadResponseModel.builder()
                .accountId(row.get("from_account_id", Long.class))
                .name(row.get("from_account_name", String.class))
                .employeeId(row.get("from_employee_id", Long.class))
                .common(row.get("from_account_common", Boolean.class))
                .build();

        AccountDsReadResponseModel toAccount = null;
        if (row.get("to_account_id") != null) {
            toAccount = AccountDsReadResponseModel.builder()
                    .accountId(row.get("to_account_id", Long.class))
                    .name(row.get("to_account_name", String.class))
                    .employeeId(row.get("to_employee_id", Long.class))
                    .common(row.get("to_account_common", Boolean.class))
                    .build();
        }

        var result = OperationDSReadResponseModel.builder()
                .operationId(row.get("operation_id", Long.class))
                .employeeId(row.get("employee_id", Long.class))
                .sum(row.get("operation_sum", Double.class))
                .description(row.get("description", String.class))
                .categoryName(row.get("category_name", String.class))
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .createdAt(row.get("operation_created_at", LocalDateTime.class))
                .status(OperationStatus.valueOf(row.get("operation_status", String.class)))
                .build();

        return addSumSign(result, Boolean.TRUE.equals(row.get("is_replenishment", Boolean.class)));
    }

    private OperationDSReadResponseModel addSumSign(OperationDSReadResponseModel ds, boolean isReplenishment) {
        if (!isReplenishment) {
            ds.setSum(-ds.getSum());
        }

        return ds;
    }
}
