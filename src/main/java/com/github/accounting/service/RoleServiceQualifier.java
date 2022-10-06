package com.github.accounting.service;

import java.util.Collection;

public interface RoleServiceQualifier<T> {
    T getServiceFromAuthorities(Collection<String> authorities);
}
