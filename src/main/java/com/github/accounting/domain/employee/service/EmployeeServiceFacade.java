package com.github.accounting.domain.employee.service;

import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeCreateRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeReadRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeServiceFacade {
    Mono<EmployeeReadCurrentResponseModel> readEmployeeProfile(EmployeeFacadeReadCurrentRequestModel requestModel);

    Flux<EmployeeReadResponseModel> readAll(EmployeeFacadeReadRequestModel requestModel);

    Mono<Void> save(EmployeeFacadeCreateRequestModel requestModel);
}
