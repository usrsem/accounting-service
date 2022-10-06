package com.github.accounting.domain.employee.dal.mapper;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class EmployeeDataSourceMapper implements BiFunction<Row, Object, EmployeeDataSource> {
    @Override
    public EmployeeDataSource apply(Row row, Object o) {
        return EmployeeDataSource.builder()
                .id(row.get("id", Long.class))
                .fullName(row.get("full_name", String.class))
                .authority(row.get("authority", String.class))
                .build();
    }
}
