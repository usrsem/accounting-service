package com.github.accounting.domain.category.model.mapper.impl;

import com.github.accounting.domain.category.dal.datasource.CategoryDataSource;
import com.github.accounting.domain.category.model.boundary.output.CategoryReadResponseModel;
import com.github.accounting.domain.category.model.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

@Component
public class SimplCategoryMapper implements CategoryMapper {
    @Override
    public CategoryReadResponseModel ds2ReadResponseModel(CategoryDataSource ds) {
        return CategoryReadResponseModel.builder()
                .id(ds.getId())
                .name(ds.getName())
                .build();
    }
}
