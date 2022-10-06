package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@SpringBootTest
class ChiefEmployeeServiceTest {

    @Autowired
    ChiefEmployeeService chiefService;

    @MockBean
    WorkerEmployeeService workerService;

    @Test
    void readEmployeeProfile_uses_worker_service() {
        var request = new EmployeeReadCurrentRequestModel(1L);
        var response = EmployeeReadCurrentResponseModel.builder()
                .id(request.employeeId())
                .build();

        when(workerService.readEmployeeProfile(request))
                .thenReturn(Mono.just(response));

        StepVerifier
                .create(chiefService.readEmployeeProfile(request))
                .expectNext(response)
                .verifyComplete();
    }

    @Test
    void readAll_uses_worker_service() {
        when(workerService.readAll())
                .thenReturn(Flux.error(new RuntimeException("Can't read all employees")));

        StepVerifier
                .create(chiefService.readAll())
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Can't read all employees"))
                .verify();
    }

    @Test
    void save_throws_on_invocation() {
        when(workerService.save(null))
                .thenReturn(Mono.error(new RuntimeException("Can't create employee")));

        StepVerifier
                .create(chiefService.save(null))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Can't create employee"))
                .verify();
    }
}