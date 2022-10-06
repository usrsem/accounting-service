package com.github.accounting.domain.category.model.mapper;

import com.github.accounting.domain.category.dal.datasource.CategoryDataSource;
import com.github.accounting.domain.category.model.boundary.output.CategoryReadResponseModel;

public interface CategoryMapper {
    CategoryReadResponseModel ds2ReadResponseModel(CategoryDataSource ds);
}
