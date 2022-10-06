package com.github.accounting.domain.operation.dal.repository.impl;

import com.github.accounting.configuration.DatabaseTestConfiguration;
import com.github.accounting.domain.operation.dal.repository.OperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.domain.operation.model.boundary.output.AccountDsReadResponseModel;
import com.github.accounting.model.OperationStatus;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataR2dbcTest
@Import(DatabaseTestConfiguration.class)
class OperationRepositoryTest {

    @Autowired
    OperationRepository repository;

    @Autowired
    DatabaseClient client;


    @TestFactory
    Stream<DynamicTest> findAllRelatedOperationsWithDetails_for_chief() {
        return verifyOperations(1L, Expected.CHIEF);
    }

    @TestFactory
    Stream<DynamicTest> findAllRelatedOperationsWithDetails_for_worker() {
        return verifyOperations(2L, Expected.WORKER);
    }

    @TestFactory
    Stream<DynamicTest> findAllRelatedOperationsWithDetails_for_accountant() {
        return verifyOperations(3L, Expected.ACCOUNTANT);
    }

    private Stream<DynamicTest> verifyOperations(Long employeeId, Set<OperationDSReadResponseModel> expected) {
        var operations = repository.findAllRelatedOperationsWithDetails(employeeId)
                .toStream().collect(Collectors.toSet());

        assertEquals(expected.size(), operations.size(), operations.stream()
                .map(OperationDSReadResponseModel::toString)
                .collect(Collectors.joining("\n")));

        return operations.stream()
                .map(actual -> DynamicTest.dynamicTest(
                        "Expected operations contains " + actual,
                        () -> {
                            Optional<OperationDSReadResponseModel> expectedDs = expected.stream()
                                    .filter(ds -> ds.getOperationId().equals(actual.getOperationId()))
                                    .findAny();
                            assertTrue(expectedDs.isPresent());
                            assertEquals(expectedDs.get(), actual, "\n" + expectedDs.get() + "\n" + actual);
                        }
                ));
    }

    @Test
    void updateOperationStatus_updates_existing_operation_status() {
        var operationId = 1L;

        try {
            StepVerifier
                    .create(repository.updateOperationStatus(operationId, OperationStatus.DELETED))
                    .verifyComplete();

            StepVerifier
                    .create(repository.findById(operationId))
                    .expectNextMatches(operation -> OperationStatus.DELETED.equals(operation.getStatus()))
                    .verifyComplete();
        } finally {
            client
                    .sql("UPDATE operation SET status='CREATED' WHERE id=:id")
                    .bind("id", operationId)
                    .fetch()
                    .rowsUpdated().block();
        }
    }

    @Test
    void updateOperationStatus_not_throw_on_nonexistent_operation() {
        StepVerifier
                .create(repository.updateOperationStatus(100L, OperationStatus.DELETED))
                .verifyComplete();
    }

    private static class Expected {

        public final static Set<OperationDSReadResponseModel> CHIEF = Set.of(
                /* Worker operations */
                new OperationDSReadResponseModel(
                        1L,
                        "Operation from common account",
                        -500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        2L,
                        "Another operation from common account",
                        -1500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        3L,
                        "Operation from worker account",
                        -700d,
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        new AccountDsReadResponseModel(5L, "Vanya", false, 4L),
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                /* Chief operations */
                new OperationDSReadResponseModel(
                        4L,
                        "Operation from chief to Tagir",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        5L,
                        "Operation from chief to Vanya",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(5L, "Vanya", false, 4L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        6L,
                        "Very old operation from chief to Tagir",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        7L,
                        "Chief withdraws from common account",
                        -150_000d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                /* Accountant operations */
                new OperationDSReadResponseModel(
                        8L,
                        "Operation from accountant to chief",
                        20_000d,
                        new AccountDsReadResponseModel(4L, "Danya", false, 3L),
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        "accounting",
                        3L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                )
        );

        public final static Set<OperationDSReadResponseModel> WORKER = Set.of(
                /* Worker operations */
                new OperationDSReadResponseModel(
                        1L,
                        "Operation from common account",
                        -500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        2L,
                        "Another operation from common account",
                        -1500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        3L,
                        "Operation from worker account",
                        -700d,
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        new AccountDsReadResponseModel(5L, "Vanya", false, 4L),
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                /* Chief operations */
                new OperationDSReadResponseModel(
                        4L,
                        "Operation from chief to Tagir",
                        500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        6L,
                        "Very old operation from chief to Tagir",
                        500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                )
        );

        public final static Set<OperationDSReadResponseModel> ACCOUNTANT = Set.of(
                /* Worker operations */
                new OperationDSReadResponseModel(
                        1L,
                        "Operation from common account",
                        -500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        2L,
                        "Another operation from common account",
                        -1500d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        3L,
                        "Operation from worker account",
                        -700d,
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        new AccountDsReadResponseModel(5L, "Vanya", false, 4L),
                        "transfer",
                        2L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                /* Chief operations */
                new OperationDSReadResponseModel(
                        4L,
                        "Operation from chief to Tagir",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        5L,
                        "Operation from chief to Vanya",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(5L, "Vanya", false, 4L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        6L,
                        "Very old operation from chief to Tagir",
                        -500d,
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        new AccountDsReadResponseModel(2L, "Tagir", false, 2L),
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        7L,
                        "Chief withdraws from common account",
                        -150_000d,
                        new AccountDsReadResponseModel(3L, "Tinkoff", true, null),
                        null,
                        "transfer",
                        1L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                /* Accountant operations */
                new OperationDSReadResponseModel(
                        8L,
                        "Operation from accountant to chief",
                        -20_000d,
                        new AccountDsReadResponseModel(4L, "Danya", false, 3L),
                        new AccountDsReadResponseModel(1L, "Andrey", false, 1L),
                        "accounting",
                        3L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                ),
                new OperationDSReadResponseModel(
                        9L,
                        "Operation from accountant to another chief",
                        -20_000d,
                        new AccountDsReadResponseModel(4L, "Danya", false, 3L),
                        new AccountDsReadResponseModel(6L, "Chief2", false, 5L),
                        "accounting",
                        3L,
                        LocalDateTime.parse("2022-02-05T23:59:59"),
                        OperationStatus.CREATED
                )
        );
    }
}