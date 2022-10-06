package com.github.accounting.domain.employee.service;

import com.github.accounting.domain.employee.model.boundary.input.EmployeeCreateRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<EmployeeReadCurrentResponseModel> readEmployeeProfile(EmployeeReadCurrentRequestModel requestModel);

    Flux<EmployeeReadResponseModel> readAll();

    Mono<Void> save(EmployeeCreateRequestModel requestModel);
}
