package com.github.accounting.domain.account.model.mapper;

import com.github.accounting.domain.account.dal.datasource.AccountDataSource;
import com.github.accounting.domain.account.model.boundary.input.AccountCreateRequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {

    @Autowired
    AccountMapper mapper;

    @Test
    void ds2ReadResponseModel() {
        var ds = new AccountDataSource(
                1L,
                "test name",
                true,
                2L,
                20d
        );

        var actual = mapper.ds2ReadResponseModel(ds);

        assertAll(
                () -> assertEquals(ds.getId(), actual.id()),
                () -> assertEquals(ds.getName(), actual.name()),
                () -> assertEquals(ds.getCommon(), actual.common()),
                () -> assertEquals(ds.getSum(), actual.sum())
        );
    }

    @Test
    void createRequestModel2ds() {
        var request = new AccountCreateRequestModel("test name", true, 3L);
        var actual = mapper.createRequestModel2ds(request);
        assertAll(
                () -> assertNull(actual.getId()),
                () -> assertEquals(request.name(), actual.getName()),
                () -> assertEquals(request.common(), actual.getCommon()),
                () -> assertEquals(request.employeeId(), actual.getEmployeeId()),
                () -> assertEquals(0, actual.getSum())
        );
    }
}