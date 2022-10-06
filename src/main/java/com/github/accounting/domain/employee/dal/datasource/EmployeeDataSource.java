package com.github.accounting.domain.employee.dal.datasource;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("employee")
@Data
@Builder
public class EmployeeDataSource {
    @Id
    @Column("id")
    private Long id;

    @Column("full_name")
    private String fullName;

    @Column("authority")
    private String authority;
}
