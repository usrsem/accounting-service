package com.github.accounting.domain.operation.controller;

import com.github.accounting.model.UserData;
import com.github.accounting.operation.model.boundary.input.*;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import com.github.accounting.operation.service.OperationServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/api/v1/operations")
@AllArgsConstructor
public class OperationController {
    private final OperationServiceFacade operationServiceFacade;
    private final UserData userData = new UserData(1, "Andrey", List.of("ROLE_CHIEF"));

    @GetMapping
    public Flux<OperationReadResponseModel> getOperations() {
        var requestModel = new OperationFacadeReadRequestModel(new OperationReadRequestModel(1), userData);
        return operationServiceFacade.readOperations(requestModel);
    }

    @PostMapping
    public Mono<Void> createOperation(@RequestBody OperationWebCreateRequestModel requestModel) {
        var request = OperationCreateRequestModel.builder()
                .categoryId(requestModel.getCategoryId())
                .fromAccountId(requestModel.getFromAccountId())
                .toAccountId(requestModel.getToAccountId())
                .description(requestModel.getDescription())
                .sum(requestModel.getSum())
                .employeeId(1L)
                .build();

        return operationServiceFacade.createOperation(new OperationFacadeCreateRequestModel(request, userData));
    }

    @DeleteMapping("/{operationId}")
    public Mono<Void> deleteOperation(@PathVariable("operationId") long operationId) {
        var requestModel = new OperationFacadeDeleteRequestModel(new OperationDeleteRequestModel(operationId), userData);
        return operationServiceFacade.deleteOperation(requestModel);
    }
}
