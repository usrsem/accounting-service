package com.github.accounting.domain.employee.dal.repository.impl;

import com.github.accounting.configuration.DatabaseTestConfiguration;
import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.model.EmployeeAuthority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DatabaseTestConfiguration.class)
class CustomEmployeeRepositoryImplTest {

    @Autowired
    EmployeeRepository repository;

    @Test
    void finds_by_accountId() {
        var expected = EmployeeDataSource.builder()
                .id(1L)
                .fullName("Andrey")
                .authority(EmployeeAuthority.CHIEF.toString())
                .build();

        StepVerifier
                .create(repository.findByAccountId(1))
                .expectNextMatches(expected::equals)
                .verifyComplete();
    }
}