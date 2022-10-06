package com.github.accounting.service.impl;

import com.github.accounting.service.RoleServiceQualifier;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Services names should match pattern <RoleName>OperationService
 * where RoleName is a name of a Spring Security user role
 * Otherwise this class can't use it
 * BeanDefinition of this class created by {@link com.github.accounting.configuration.RoleServiceQualifierInjectorBeanFactoryPostProcessor}
 *
 * @see ChainOfResponsibilityRoleServiceQualifier#getServiceFromAuthorities(Collection)
 */
@AllArgsConstructor
public class ChainOfResponsibilityRoleServiceQualifier<T> implements RoleServiceQualifier<T> {

    private final Map<String, T> services;
    private final String genericTypeName;

    @Override
    public T getServiceFromAuthorities(Collection<String> authorities) {
        List<String> roles = authorities.stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();

        if (roles.size() != 1) {
            throw new RuntimeException("Only one role must be specified, got " + roles);
        }

        var role = roles.get(0).replace("ROLE_", "");
        var serviceName = role.toLowerCase() + genericTypeName;

        var service = services.get(serviceName);
        if (service == null) {
            throw new RuntimeException("Service for role " + role + " not found");
        }

        return service;
    }

}
