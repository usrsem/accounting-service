package com.github.accounting.operation.dal.repository.impl;

import com.github.accounting.operation.dal.datasource.OperationDSReadResponseModel;
import com.github.accounting.operation.dal.mapper.OperationDataSourceMapper;
import com.github.accounting.operation.dal.repository.CustomOperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class CustomOperationRepositoryImpl implements CustomOperationRepository {

    private final DatabaseClient client;
    private final OperationDataSourceMapper mapper;

    @Override
    public Flux<OperationDSReadResponseModel> findAllWithDetails() {
        var sql = """
                SELECT o.id           as operation_id,
                       o.employee_id  as employee_id,
                       ta.employee_id as to_employee_id,
                       o.sum          as operation_sum,
                       o.created_at   as operation_created_at,
                       c.name         as category_name,
                       fa.name        as from_account_name,
                       ta.name        as to_account_name,
                       e.authority    as employee_authority,
                       description
                from operation as o
                         LEFT JOIN category c ON o.category_id = c.id
                         LEFT JOIN account fa on o.from_account_id = fa.id
                         LEFT JOIN account ta on o.to_account_id = ta.id
                         LEFT JOIN employee e on o.employee_id = e.id;
                """;

        return client
                .sql(sql)
                .map(mapper::apply)
                .all();
    }
}
