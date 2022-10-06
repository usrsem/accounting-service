package com.github.accounting.domain.operation.dal.datasource;

import com.github.accounting.model.OperationStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

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

    @Column("created_at")
    private LocalDateTime createdAd;

    @Column("status")
    private OperationStatus status;
}
