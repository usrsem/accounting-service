package com.github.accounting.operation.dal.mapper;

import com.github.accounting.operation.dal.datasource.JoinedOperationDataSource;
import io.r2dbc.spi.Row;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class OperationDataSourceMapper implements BiFunction<Row, Object, JoinedOperationDataSource> {

    @Override
    @SneakyThrows
    public JoinedOperationDataSource apply(Row row, Object o) {
        var operation_id = Objects.requireNonNull(row.get("operation_id", Long.class));
        var operation_sum = Objects.requireNonNull(row.get("operation_sum", Double.class));
        var dateAsString = Objects.requireNonNull(row.get("operation_created_at")).toString();
        var createdAt = LocalDateTime.parse(dateAsString);

        return JoinedOperationDataSource.builder()
                .operationId(operation_id)
                .sum(operation_sum)
                .description(row.get("description", String.class))
                .categoryName(row.get("category_name", String.class))
                .fromAccountName(row.get("from_account_name", String.class))
                .toAccountName(row.get("to_account_name", String.class))
                .createdAt(createdAt)
                .build();
    }
}
