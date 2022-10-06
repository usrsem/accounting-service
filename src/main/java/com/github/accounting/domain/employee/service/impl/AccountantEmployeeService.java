package com.github.accounting.domain.employee.service.impl;

import com.github.accounting.domain.employee.dal.repository.EmployeeRepository;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeCreateRequestModel;
import com.github.accounting.domain.employee.model.boundary.input.EmployeeReadCurrentRequestModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import com.github.accounting.domain.employee.model.mapper.EmployeeMapper;
import com.github.accounting.domain.employee.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AccountantEmployeeService implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    public Mono<EmployeeReadCurrentResponseModel> readEmployeeProfile(EmployeeReadCurrentRequestModel requestModel) {
        return Mono.just(requestModel.employeeId())
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new RuntimeException("Employee not found")))
                .map(mapper::ds2ReadCurrentResponseModel);
    }

    @Override
    public Flux<EmployeeReadResponseModel> readAll() {
        return repository
                .findAll()
                .map(mapper::ds2ReadResponseModel);
    }

    @Override
    public Mono<Void> save(EmployeeCreateRequestModel requestModel) {
        return Mono.error(new RuntimeException("Can't create new employee"));
    }
}
