package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.model.boundary.input.EmployeeCreateRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import com.github.accounting.domain.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ChiefEmployeeService implements EmployeeService {

    private final EmployeeService workerEmployeeService;

    @Override
    public Mono<EmployeeReadCurrentResponseModel> readEmployeeProfile(EmployeeReadCurrentRequestModel requestModel) {
        return workerEmployeeService.readEmployeeProfile(requestModel);
    }

    @Override
    public Flux<EmployeeReadResponseModel> readAll() {
        return workerEmployeeService.readAll();
    }

    @Override
    public Mono<Void> save(EmployeeCreateRequestModel requestModel) {
        return workerEmployeeService.save(requestModel);
    }
}
