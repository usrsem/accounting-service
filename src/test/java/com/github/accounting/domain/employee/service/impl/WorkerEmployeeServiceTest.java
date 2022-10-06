package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
class WorkerEmployeeServiceTest {

    @Autowired
    WorkerEmployeeService employeeService;
    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    void readEmployeeProfile_reads_current_employee() {
        var request = new EmployeeReadCurrentRequestModel(1L);
        when(employeeRepository.findById(request.employeeId()))
                .thenReturn(Mono.just(EmployeeDataSource.builder().id(request.employeeId()).build()));
        StepVerifier
                .create(employeeService.readEmployeeProfile(request))
                .expectNextMatches(e -> request.employeeId().equals(e.id()))
                .verifyComplete();
    }

    @Test
    void readEmployeeProfile_throws_when_not_found() {
        var request = new EmployeeReadCurrentRequestModel(1L);
        when(employeeRepository.findById(request.employeeId()))
                .thenReturn(Mono.empty());
        StepVerifier
                .create(employeeService.readEmployeeProfile(request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && "Not found".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    void readAll_throws_on_invocation() {
        StepVerifier
                .create(employeeService.readAll())
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && "Can't read all employees".equals(throwable.getMessage()))
                .verify();
    }

    @Test
    void save_throws_on_invocation() {
        StepVerifier
                .create(employeeService.save(null))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && "Can't create employee".equals(throwable.getMessage()))
                .verify();
    }
}