package com.github.accounting.domain.account.model.mapper.impl;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.domain.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.domain.account.model.mapper.AccountMapper;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccountMapper implements AccountMapper {
    @Override
    public AccountReadResponseModel ds2ReadResponseModel(AccountDataSource accountDataSource) {
        return AccountReadResponseModel.builder()
                .id(accountDataSource.getId())
                .name(accountDataSource.getName())
                .common(accountDataSource.getCommon())
                .sum(accountDataSource.getSum())
                .build();
    }

    @Override
    public AccountDataSource createRequestModel2ds(AccountCreateRequestModel requestModel) {
        return AccountDataSource.builder()
                .name(requestModel.name())
                .common(requestModel.common())
                .employeeId(requestModel.employeeId())
                .sum(0d)
                .build();
    }
}
