package com.github.accounting.configuration;

import com.github.accounting.domain.employee.dal.mapper.EmployeeDataSourceMapper;
import com.github.accounting.domain.operation.dal.mapper.OperationDataSourceMapper;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@TestConfiguration
public class DatabaseTestConfiguration {

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource("schema.sql"),
                new ClassPathResource("test-data.sql")
        ));
        return initializer;
    }

    @Bean
    EmployeeDataSourceMapper employeeDataSourceMapper() {
        return new EmployeeDataSourceMapper();
    }

    @Bean
    OperationDataSourceMapper operationDataSourceMapper() {
        return new OperationDataSourceMapper();
    }
}
