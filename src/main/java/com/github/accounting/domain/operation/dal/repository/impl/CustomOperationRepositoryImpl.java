package com.github.accounting.domain.operation.dal.repository.impl;

import com.github.accounting.domain.operation.dal.mapper.OperationDataSourceMapper;
import com.github.accounting.domain.operation.dal.repository.CustomOperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.model.OperationStatus;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CustomOperationRepositoryImpl implements CustomOperationRepository {

    private final DatabaseClient client;
    private final OperationDataSourceMapper mapper;

    @Override
    public Flux<OperationDSReadResponseModel> findAllRelatedOperationsWithDetails(Long employeeId) {
        var sql = """
                SELECT o.id                                                                          AS operation_id,
                       o.employee_id                                                                 AS employee_id,
                       ta.employee_id                                                                AS to_employee_id,
                       o.sum                                                                         AS operation_sum,
                       o.created_at                                                                  AS operation_created_at,
                       c.name                                                                        AS category_name,
                       fa.id                                                                         AS from_account_id,
                       fa.name                                                                       AS from_account_name,
                       fa.employee_id                                                                AS from_employee_id,
                       fa.common                                                                     AS from_account_common,
                       ta.id                                                                         AS to_account_id,
                       ta.name                                                                       AS to_account_name,
                       ta.employee_id                                                                AS to_employee_id,
                       ta.common                                                                     AS to_account_common,
                       o.status                                                                      AS operation_status,
                       description                                                                   AS description,
                       NOT ta.common AND ta.employee_id IS NOT NULL AND ta.employee_id = :employeeId AS is_replenishment
                FROM operation AS o
                         LEFT JOIN category c ON o.category_id = c.id
                         LEFT JOIN account fa ON o.from_account_id = fa.id
                         LEFT JOIN account ta ON o.to_account_id = ta.id
                         LEFT JOIN employee e ON o.employee_id = e.id
                         LEFT JOIN employee ce ON ce.id = :employeeId
                WHERE o.employee_id = :employeeId
                   OR ta.employee_id = :employeeId
                   OR ((ta.employee_id IS NULL
                    OR ce.authority != (SELECT employee.authority FROM employee WHERE employee.id = ta.employee_id))
                    AND ((SELECT COUNT(1) > 0 FROM authority_relation AS er WHERE er.master_authority = ce.authority)
                        AND (fa.employee_id != ce.id OR fa.common)))
                """;

        return client
                .sql(sql)
                .bind("employeeId", employeeId)
                .map(mapper::apply)
                .all();
    }

    @Override
    public Mono<Void> updateOperationStatus(Long operationId, OperationStatus status) {
        var sql = """
                UPDATE operation
                SET status = :status
                WHERE id = :operationId
                """;

        return client
                .sql(sql)
                .bind("operationId", operationId)
                .bind("status", status.toString())
                .then();
    }
}
