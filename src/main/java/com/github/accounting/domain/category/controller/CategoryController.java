package com.github.accounting.domain.category.controller;

import com.github.accounting.domain.category.model.boundary.input.CategoryReadRequestModel;
import com.github.accounting.domain.category.model.boundary.output.CategoryReadResponseModel;
import com.github.accounting.domain.category.service.CategoryService;
import com.github.accounting.model.UserData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserData userData = new UserData(1L, "Andrey", Set.of("ROLE_CHIEF"));

    @GetMapping
    public Flux<CategoryReadResponseModel> getAvailableCategories() {
        var requestModel = new CategoryReadRequestModel(1L);
        return categoryService.readRelated(requestModel);
    }
}
