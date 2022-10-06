package com.github.accounting.domain.category.service.impl;

import com.github.accounting.domain.category.dal.repository.CategoryRepository;
import com.github.accounting.domain.category.model.boundary.input.CategoryReadRequestModel;
import com.github.accounting.domain.category.model.boundary.output.CategoryReadResponseModel;
import com.github.accounting.domain.category.model.mapper.CategoryMapper;
import com.github.accounting.domain.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class CommonCategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public Flux<CategoryReadResponseModel> readRelated(CategoryReadRequestModel requestModel) {
        return Flux.just(requestModel.employeeId())
                .flatMap(categoryRepository::findAllRelated)
                .map(mapper::ds2ReadResponseModel);
    }
}
