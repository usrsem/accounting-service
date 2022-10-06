package com.github.accounting.service.impl;

import com.github.accounting.service.RoleServiceQualifier;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChainOfResponsibilityRoleServiceQualifierTest {

    final RoleServiceQualifier<Object> underTest = new ChainOfResponsibilityRoleServiceQualifier<>(
            new HashMap<>(),
            "Object"
    );

    @Test
    void throws_on_more_then_2_roles() {
        assertThrows(
                RuntimeException.class,
                () -> underTest.getServiceFromAuthorities(Set.of("ROLE_1", "ROLE_2"))
        );
    }

    @Test
    void throws_on_less_then_1_role() {
        assertThrows(
                RuntimeException.class,
                () -> underTest.getServiceFromAuthorities(Set.of("NOT_A_ROLE"))
        );
    }

    @Test
    void throws_on_service_not_found() {
        assertThrows(
                RuntimeException.class,
                () -> underTest.getServiceFromAuthorities(Set.of("ROLE_1"))
        );
    }

    @TestFactory
    Stream<DynamicTest> returns_service_for_each_role() {
        var stringObjectMap = Map.of(
                "1Object", new Object(),
                "2Object", new Object(),
                "3Object", new Object(),
                "4Object", new Object(),
                "5Object", new Object()
        );

        var service = new ChainOfResponsibilityRoleServiceQualifier<>(
                new HashMap<>(stringObjectMap),
                "Object"
        );

        return stringObjectMap.keySet().stream()
                .map(expected -> DynamicTest.dynamicTest(
                        "Should return values for " + expected,
                        () -> {
                            var roleName = "ROLE_" + expected.replace("Object", "");
                            var actual = service.getServiceFromAuthorities(Set.of(roleName));
                            assertEquals(actual, stringObjectMap.get(expected));
                        }));
    }
}