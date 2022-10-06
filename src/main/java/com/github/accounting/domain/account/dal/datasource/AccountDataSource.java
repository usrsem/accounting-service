package com.github.accounting.domain.account.dal.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("account")
@Data
@Builder
@AllArgsConstructor
public class AccountDataSource {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("common")
    private Boolean common;

    @Column("employee_id")
    private Long employeeId;

    @Column("sum")
    private Double sum;
}
