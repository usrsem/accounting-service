package com.github.accounting.model;

import java.util.Collection;

public record UserData(long id, String name, Collection<String> authorities) {
    public EmployeeAuthority getRole() {
        var role = authorities.stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .findAny();
        if (role.isEmpty()) {
            throw new RuntimeException("User have not any role");
        }
        return EmployeeAuthority.valueOf(role.get());
    }
}
