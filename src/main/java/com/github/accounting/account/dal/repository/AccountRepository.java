package com.github.accounting.account.dal.repository;

import com.github.accounting.account.dal.datasource.AccountDataSource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountRepository extends ReactiveCrudRepository<AccountDataSource, Long> {
}
