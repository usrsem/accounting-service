package com.github.accounting.domain.category.dal.repository;

import com.github.accounting.domain.category.dal.datasource.CategoryDataSource;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryDataSource, Integer> {
    Mono<CategoryDataSource> findByName(String name);

    @Query("""
            SELECT c.id,
                   c.name
            FROM category as c
                     LEFT JOIN category_relation cr on c.id = cr.category_id
            WHERE cr.authority = (SELECT authority FROM employee WHERE employee.id = ?1)
            """)
    Flux<CategoryDataSource> findAllRelated(Long employeeId);
}
