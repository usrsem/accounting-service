package com.github.accounting.domain.employee.dal.repository.impl;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.dal.mapper.EmployeeDataSourceMapper;
import com.github.accounting.domain.employee.dal.repository.CustomEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    private final DatabaseClient client;
    private final EmployeeDataSourceMapper mapper;

    @Override
    public Mono<EmployeeDataSource> findByAccountId(long accountId) {
        var sql = """
                SELECT e.id, e.full_name, e.authority FROM account AS a
                LEFT JOIN (employee) AS e ON a.employee_id = e.id
                WHERE a.id = :accountId
                """;

        return client
                .sql(sql)
                .bind("accountId", accountId)
                .map(mapper::apply)
                .first();
    }
}
