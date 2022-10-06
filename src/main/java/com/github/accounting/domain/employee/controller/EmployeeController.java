package com.github.accounting.domain.employee.controller;

import com.github.accounting.domain.employee.model.boundary.input.*;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import com.github.accounting.domain.employee.service.EmployeeServiceFacade;
import com.github.accounting.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeServiceFacade serviceFacade;
    private final UserData userData = new UserData(1, "Andrey", List.of("ROLE_CHIEF"));

    @GetMapping("/me")
    public Mono<EmployeeReadCurrentResponseModel> getMe() {
        var requestModel = new EmployeeFacadeReadCurrentRequestModel(
                new EmployeeReadCurrentRequestModel(userData.id()),
                userData
        );
        return serviceFacade.readEmployeeProfile(requestModel);
    }

    @GetMapping
    public Flux<EmployeeReadResponseModel> readAll() {
        var requestModel = new EmployeeFacadeReadRequestModel(userData);
        return serviceFacade.readAll(requestModel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> save(@RequestBody EmployeeCreateRequestModel requestModel) {
        var request = new EmployeeFacadeCreateRequestModel(requestModel, userData);
        return serviceFacade.save(request);
    }
}
