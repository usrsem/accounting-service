package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
class AccountantEmployeeServiceTest {

    @Autowired
    AccountantEmployeeService employeeService;
    @MockBean
    EmployeeRepository employeeRepository;

    @Test
    void readEmployeeProfile_reads_current_employee() {
        var request = new EmployeeReadCurrentRequestModel(1L);
        var employeeDs = EmployeeDataSource.builder()
                .id(request.employeeId())
                .build();

        when(employeeRepository.findById(request.employeeId()))
                .thenReturn(Mono.just(employeeDs));

        StepVerifier
                .create(employeeService.readEmployeeProfile(request))
                .expectNextMatches(employee -> request.employeeId().equals(employee.id()))
                .verifyComplete();
    }

    @Test
    void readEmployeeProfile_throws_when_not_found() {
        var request = new EmployeeReadCurrentRequestModel(2L);
        when(employeeRepository.findById(request.employeeId()))
                .thenReturn(Mono.empty());

        StepVerifier
                .create(employeeService.readEmployeeProfile(request))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Employee not found"))
                .verify();
    }

    @Test
    void readAll_return_all_employees() {
        var employees = Flux
                .range(0, 10)
                .map(id -> EmployeeDataSource.builder().id(Long.valueOf(id)).build());

        when(employeeRepository.findAll())
                .thenReturn(employees);

        StepVerifier
                .create(employeeService.readAll())
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void save_throws_on_invocation() {
        StepVerifier
                .create(employeeService.save(null))
                .verifyError();
    }
}