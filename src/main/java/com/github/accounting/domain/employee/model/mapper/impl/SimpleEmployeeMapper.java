package com.github.accounting.domain.employee.model.mapper.impl;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;
import com.github.accounting.domain.employee.model.mapper.EmployeeMapper;
import org.springframework.stereotype.Component;

@Component
public class SimpleEmployeeMapper implements EmployeeMapper {
    @Override
    public EmployeeReadResponseModel ds2ReadResponseModel(EmployeeDataSource employeeDataSource) {
        return EmployeeReadResponseModel.builder()
                .id(employeeDataSource.getId())
                .fullname(employeeDataSource.getFullName())
                .authority(employeeDataSource.getAuthority())
                .build();
    }

    @Override
    public EmployeeReadCurrentResponseModel ds2ReadCurrentResponseModel(EmployeeDataSource employeeDataSource) {
        return EmployeeReadCurrentResponseModel.builder()
                .id(employeeDataSource.getId())
                .fullname(employeeDataSource.getFullName())
                .authority(employeeDataSource.getAuthority())
                .build();
    }
}
