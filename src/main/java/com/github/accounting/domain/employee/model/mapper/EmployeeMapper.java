package com.github.accounting.domain.employee.model.mapper;

import com.github.accounting.domain.employee.dal.datasource.EmployeeDataSource;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadCurrentResponseModel;
import com.github.accounting.domain.employee.model.boundary.output.EmployeeReadResponseModel;

public interface EmployeeMapper {
    EmployeeReadResponseModel ds2ReadResponseModel(EmployeeDataSource employeeDataSource);

    EmployeeReadCurrentResponseModel ds2ReadCurrentResponseModel(EmployeeDataSource employeeDataSource);
}
