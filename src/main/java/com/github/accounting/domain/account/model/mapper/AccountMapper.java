package com.github.accounting.domain.account.model.mapper;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import com.github.accounting.domain.account.model.boundary.output.AccountReadResponseModel;

public interface AccountMapper {
    AccountReadResponseModel ds2ReadResponseModel(AccountDataSource accountDataSource);

    AccountDataSource createRequestModel2ds(AccountCreateRequestModel requestModel);
}
