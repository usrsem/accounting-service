package com.github.accounting.domain.operation.controller;

import com.github.accounting.domain.operation.model.boundary.input.OperationCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationDeleteRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationReadRequestModel;
import com.github.accounting.domain.operation.model.boundary.input.OperationWebCreateRequestModel;
import com.github.accounting.domain.operation.model.boundary.output.OperationReadResponseModel;
import com.github.accounting.domain.operation.service.OperationService;
import com.github.accounting.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/api/v1/operations")
@AllArgsConstructor
public class OperationController {
    private final OperationService operationService;
    private final UserData userData = new UserData(1, "Andrey", List.of("ROLE_CHIEF"));

    @GetMapping
    public Flux<OperationReadResponseModel> getOperations() {
        var requestModel = new OperationReadRequestModel(1);
        return operationService.readOperations(requestModel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createOperation(@RequestBody OperationWebCreateRequestModel requestModel) {
        var request = OperationCreateRequestModel.builder()
                .categoryId(requestModel.getCategoryId())
                .fromAccountId(requestModel.getFromAccountId())
                .toAccountId(requestModel.getToAccountId())
                .description(requestModel.getDescription())
                .sum(requestModel.getSum())
                .employeeId(1L)
                .role(userData.getRole())
                .build();

        return operationService.createOperation(request);
    }

    @DeleteMapping("/{operationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOperation(@PathVariable("operationId") Long operationId) {
        // TODO: 9/20/2022 Add null checking for operationID
        var requestModel = new OperationDeleteRequestModel(operationId, userData.getRole());
        return operationService.deleteOperation(requestModel);
    }
}
