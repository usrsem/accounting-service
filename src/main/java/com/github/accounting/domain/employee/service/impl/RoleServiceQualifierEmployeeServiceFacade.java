package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeCreateRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeFacadeReadRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import com.github.accounting.domain.employee.service.EmployeeService;
import com.github.accounting.domain.employee.service.EmployeeServiceFacade;
import com.github.accounting.service.RoleServiceQualifier;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RoleServiceQualifierEmployeeServiceFacade implements EmployeeServiceFacade {

    private final RoleServiceQualifier<EmployeeService> employeeServiceQualifier;

    @Override
    public Mono<EmployeeReadCurrentResponseModel> readEmployeeProfile(
            EmployeeFacadeReadCurrentRequestModel requestModel
    ) {
        return employeeServiceQualifier
                .getServiceFromAuthorities(requestModel.userData().authorities())
                .readEmployeeProfile(requestModel.request());
    }

    @Override
    public Flux<EmployeeReadResponseModel> readAll(EmployeeFacadeReadRequestModel requestModel) {
        return employeeServiceQualifier
                .getServiceFromAuthorities(requestModel.userData().authorities())
                .readAll();
    }

    @Override
    public Mono<Void> save(EmployeeFacadeCreateRequestModel requestModel) {
        return employeeServiceQualifier
                .getServiceFromAuthorities(requestModel.userData().authorities())
                .save(requestModel.request());
    }
}
