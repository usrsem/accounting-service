package com.github.accounting.domain.account.dal.repository;

import com.github.accounting.configuration.DatabaseTestConfiguration;
import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataR2dbcTest
@Import(DatabaseTestConfiguration.class)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    final static AccountDataSource account = new AccountDataSource(6L,
            "Chief1",
            false,
            5L,
            5000d);

    @Test
    void findByName() {
        StepVerifier
                .create(accountRepository.findByName(account.getName()))
                .expectNextMatches(account::equals)
                .verifyComplete();
    }

    @Test
    void findByEmployeeId() {
        StepVerifier
                .create(accountRepository.findByEmployeeId(account.getEmployeeId()))
                .expectNextMatches(account::equals)
                .verifyComplete();
    }

    @TestFactory
    Stream<DynamicTest> findAllRelated_for_chief() {
        var samples = Set.of(
                new AccountDataSource(1L, "Andrey", false, 1L, 5000d),
                new AccountDataSource(2L, "Tagir", false, 2L, 10_000d),
                new AccountDataSource(3L, "Tinkoff", true, null, 100_000d),
                new AccountDataSource(5L, "Vanya", false, 4L, 100d)
        );

        return verifySamples(samples, 1L);
    }

    @TestFactory
    Stream<DynamicTest> findAllRelated_for_worker() {
        var samples = Set.of(
                new AccountDataSource(2L, "Tagir", false, 2L, 10_000d),
                new AccountDataSource(3L, "Tinkoff", true, null, 100_000d)
        );

        return verifySamples(samples, 2L);
    }

    @TestFactory
    Stream<DynamicTest> findAllRelated_for_accountant() {
        var samples = Set.of(
                new AccountDataSource(1L, "Andrey", false, 1L, 5000d),
                new AccountDataSource(2L, "Tagir", false, 2L, 10_000d),
                new AccountDataSource(3L, "Tinkoff", true, null, 100_000d),
                new AccountDataSource(4L, "Danya", false, 3L, Double.POSITIVE_INFINITY),
                new AccountDataSource(5L, "Vanya", false, 4L, 100d),
                new AccountDataSource(6L, "Chief1", false, 5L, 5000d)
        );

        return verifySamples(samples, 3L);
    }

    @TestFactory
    private Stream<DynamicTest> verifySamples(Collection<AccountDataSource> samples, Long employeeId) {
        var accounts = accountRepository.findAllRelated(employeeId).toStream().collect(Collectors.toSet());
        assertEquals(samples.size(), accounts.size());
        return samples.stream()
                .map(sample -> DynamicTest.dynamicTest(
                        "Actual contains " + sample,
                        () -> assertTrue(accounts.contains(sample), accounts.toString())
                ));
    }
}