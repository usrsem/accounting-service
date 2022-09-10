package com.github.accounting.operation.service.impl;

import com.github.accounting.operation.model.boundary.input.OperationFacadeCreateRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationFacadeDeleteRequestModel;
import com.github.accounting.operation.model.boundary.input.OperationFacadeReadRequestModel;
import com.github.accounting.operation.model.boundary.output.OperationReadResponseModel;
import com.github.accounting.operation.service.OperationService;
import com.github.accounting.operation.service.OperationServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Services names should match pattern <RoleName>OperationService
 * where RoleName is a name of a Spring Security user role
 * Otherwise this class can't use it
 * 
 * @see ChainOfResponsibilityOperationServiceFacade#getOperationService
 */
@Service
@AllArgsConstructor
public class ChainOfResponsibilityOperationServiceFacade implements OperationServiceFacade {
    private final Map<String, OperationService> services;

    @Override
    public Flux<OperationReadResponseModel> readOperations(OperationFacadeReadRequestModel requestModel) {
        return getOperationService(requestModel.userData().authorities())
                .readOperations(requestModel.requestModel());
    }

    @Override
    public Mono<Void> createOperation(OperationFacadeCreateRequestModel requestModel) {
        return getOperationService(requestModel.userData().authorities()).createOperation(requestModel.requestModel());
    }

    @Override
    public Mono<Void> deleteOperation(OperationFacadeDeleteRequestModel requestModel) {
        return getOperationService(requestModel.userData().authorities()).deleteOperation(requestModel.requestModel());
    }

    private OperationService getOperationService(Collection<String> authorities) {
        List<String> roles = authorities.stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(Object::toString)
                .toList();

        if (roles.size() != 1) {
            throw new RuntimeException("Only one role must be specified, got " + roles);
        }

        var role = roles.get(0).replace("ROLE_", "");
        var serviceName = role.toLowerCase() + "OperationService";

        var service = services.get(serviceName);
        if (service == null) {
            throw new RuntimeException("Service for role " + role + " not found");
        }

        return service;
    }
}
