package com.github.accounting.domain.category.service;

import com.github.accounting.domain.category.model.boundary.input.CategoryReadRequestModel;
import com.github.accounting.domain.category.model.boundary.output.CategoryReadResponseModel;
import reactor.core.publisher.Flux;

public interface CategoryService {
   Flux<CategoryReadResponseModel> readRelated(CategoryReadRequestModel requestModel);
}
