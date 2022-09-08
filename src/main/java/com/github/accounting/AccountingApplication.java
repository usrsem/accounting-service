package com.github.accounting;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableReactiveMethodSecurity
@EnableWebFlux
public class AccountingApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
    }

}
