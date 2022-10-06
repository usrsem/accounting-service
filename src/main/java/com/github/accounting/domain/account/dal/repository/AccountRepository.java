package com.github.accounting.domain.account.dal.repository;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveCrudRepository<AccountDataSource, Long> {

    Mono<AccountDataSource> findByName(String name);

    Mono<AccountDataSource> findByEmployeeId(Long employeeId);

    @Query(value = """
            SELECT a.*, e.authority
            FROM account AS a
                     LEFT JOIN employee e ON a.employee_id = e.id
            WHERE a.employee_id = ?1
               OR a.common
               OR (SELECT COUNT(1) > 0
                   FROM authority_relation AS er
                   WHERE er.slave_authority = e.authority
                     AND er.master_authority = (SELECT ce.authority FROM employee AS ce WHERE ce.id = ?1))
            """)
    Flux<AccountDataSource> findAllRelated(Long employeeId);
}
