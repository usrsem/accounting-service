package com.github.accounting.domain.operation.service.impl;

import com.github.accounting.domain.operation.dal.datasource.OperationDataSource;
import com.github.accounting.domain.operation.dal.repository.OperationRepository;
import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDSReadResponseModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationReadRequestModel;
import com.github.accounting.domain.operation.model.boundary.output.AccountDsReadResponseModel;
import com.github.accounting.domain.operation.model.mapper.OperationMapper;
import com.github.accounting.domain.operation.service.OperationDepositProcessor;
import com.github.accounting.domain.operation.service.OperationPreprocessor;
import com.github.accounting.model.EmployeeAuthority;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommonOperationServiceTest {

    @Autowired
    OperationMapper mapper;

    final OperationRepository operationRepositoryMock = mock(OperationRepository.class);
    final OperationPreprocessor preprocessorMock = mock(OperationPreprocessor.class);
    final OperationDepositProcessor operationDepositProcessorMock = mock(OperationDepositProcessor.class);
    @SuppressWarnings("unchecked")
    final Map<String, OperationPreprocessor> preprocessorMapMock = (Map<String, OperationPreprocessor>) mock(HashMap.class);
    final CommonOperationService operationService = new CommonOperationService(operationDepositProcessorMock,
            preprocessorMapMock,
            operationRepositoryMock,
            new OperationMapper()
    );

    @Test
    void fails_on_invalid_OperationPreprocessor() {
        when(preprocessorMapMock.get(anyString())).thenReturn(null);
        assertThrows(RuntimeException.class, operationService::checkPreprocessorsContents);
    }

    @Test
    void readOperations_returns_related_info() {
        var responseModel = OperationDSReadResponseModel.builder()
                .operationId(1L)
                .employeeId(2L)
                .sum(12d)
                .description("")
                .categoryName("")
                .createdAt(LocalDateTime.now())
                .toAccount(AccountDsReadResponseModel.builder()
                        .accountId(1L)
                        .name("")
                        .common(true)
                        .build())
                .fromAccount(AccountDsReadResponseModel.builder()
                        .accountId(1L)
                        .name("")
                        .common(true)
                        .build())
                .build();

        var expected = mapper.dsReadResponseModel2ReadResponseModel(responseModel);

        when(operationRepositoryMock.findAllRelatedOperationsWithDetails(anyLong()))
                .thenReturn(Flux.just(responseModel));

        StepVerifier
                .create(operationService.readOperations(new OperationReadRequestModel(1L)))
                .expectNextMatches(expected::equals)
                .verifyComplete();
    }

    @Test
    void createOperation_saves_operation() {
        var requestModel = OperationCreateRequestModel.builder()
                .role(EmployeeAuthority.WORKER)
                .build();

        when(preprocessorMapMock.get(anyString())).thenReturn(preprocessorMock);
        when(operationDepositProcessorMock.createDeposit(any())).thenReturn(Mono.empty());
        when(preprocessorMock.preprocessCreation(any())).thenReturn(Mono.just(requestModel));
        when(operationRepositoryMock.save(any())).thenReturn(Mono.empty());

        StepVerifier
                .create(operationService.createOperation(requestModel))
                .verifyComplete();

        Mockito.verify(preprocessorMock).preprocessCreation(any());
        Mockito.verify(operationRepositoryMock).save(any());
    }

    @Test
    void createOperation_throws_on_wrong_operation() {
        var requestModel = OperationCreateRequestModel.builder()
                .role(EmployeeAuthority.WORKER)
                .build();

        when(preprocessorMapMock.get(anyString())).thenReturn(preprocessorMock);
        when(preprocessorMock.preprocessCreation(any())).thenThrow(new RuntimeException());

        StepVerifier
                .create(operationService.createOperation(requestModel))
                .verifyError();

        Mockito.verify(preprocessorMock).preprocessCreation(any());
        Mockito.verify(operationRepositoryMock, times(0)).save(any());
    }

    @Test
    void deleteOperation_deletes_operation() {
        var requestModel = new OperationDeleteRequestModel(1L, EmployeeAuthority.WORKER);

        when(preprocessorMapMock.get(anyString())).thenReturn(preprocessorMock);
        when(preprocessorMock.preprocessDeletion(any())).thenReturn(Mono.just(requestModel));
        when(operationRepositoryMock.findById(anyLong())).thenReturn(Mono.just(OperationDataSource.builder().build()));
        when(operationDepositProcessorMock.rollbackDeposit(any())).thenReturn(Mono.empty());
        when(operationRepositoryMock.updateOperationStatus(anyLong(), any())).thenReturn(Mono.empty());

        StepVerifier
                .create(operationService.deleteOperation(requestModel))
                .verifyComplete();

        verify(operationDepositProcessorMock).rollbackDeposit(any());
        verify(operationRepositoryMock).updateOperationStatus(anyLong(), any());
    }

    @Test
    void deleteOperation_throws_on_wrong_operation() {
        var requestModel = new OperationDeleteRequestModel(1L, EmployeeAuthority.WORKER);

        when(preprocessorMapMock.get(anyString())).thenReturn(preprocessorMock);
        when(operationDepositProcessorMock.rollbackDeposit(any())).thenReturn(Mono.empty());
        when(preprocessorMock.preprocessDeletion(any())).thenThrow(new RuntimeException("Wrong request"));

        StepVerifier
                .create(operationService.deleteOperation(requestModel))
                .verifyError();

        verify(operationRepositoryMock, times(0)).delete(any());
        verify(operationDepositProcessorMock, times(0)).rollbackDeposit(any());
    }
}