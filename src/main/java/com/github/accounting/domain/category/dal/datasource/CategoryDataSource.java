package com.github.accounting.domain.category.dal.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table("category")
@AllArgsConstructor
@Getter
public class CategoryDataSource {
    @Id
    @Column("id")
    private int id;

    @Column("name")
    private String name;
}
