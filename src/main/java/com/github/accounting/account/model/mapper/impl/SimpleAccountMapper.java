package com.github.accounting.account.model.mapper.impl;

import com.github.accounting.account.dal.datasource.AccountDataSource;
import com.github.accounting.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.account.model.boundary.output.AccountReadResponseModel;
import com.github.accounting.account.model.mapper.AccountMapper;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccountMapper implements AccountMapper {
    @Override
    public AccountReadResponseModel dS2ReadResponseModel(AccountDataSource accountDataSource) {
        return AccountReadResponseModel.builder()
                .id(accountDataSource.getId())
                .name(accountDataSource.getName())
                .common(accountDataSource.getCommon())
                .build();
    }

    @Override
    public AccountDataSource createRequestModel2Ds(AccountCreateRequestModel requestModel) {
        return AccountDataSource.builder()
                .name(requestModel.name())
                .common(requestModel.common())
                .employeeId(requestModel.employeeId())
                .build();
    }
}
