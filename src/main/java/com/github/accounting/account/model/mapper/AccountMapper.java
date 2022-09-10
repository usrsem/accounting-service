package com.github.accounting.account.model.mapper;

import com.github.accounting.account.dal.datasource.AccountDataSource;
import com.github.accounting.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.account.model.boundary.output.AccountReadResponseModel;

public interface AccountMapper {
    AccountReadResponseModel dS2ReadResponseModel(AccountDataSource accountDataSource);

    AccountDataSource createRequestModel2Ds(AccountCreateRequestModel requestModel);
}
