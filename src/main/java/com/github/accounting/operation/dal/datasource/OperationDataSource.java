package com.github.accounting.operation.dal.datasource;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("operation")
@Data
@Builder
public class OperationDataSource {
    @Id
    @Column("id")
    private Long id;

    @Column("from_account_id")
    private Long fromAccountId;

    @Column("to_account_id")
    private Long toAccountId;

    @Column("category_id")
    private Integer categoryId;

    @Column("description")
    private String description;

    @Column("employee_id")
    private Long employeeId;

    @Column("sum")
    private Float sum;
}
