package com.github.accounting.domain.category.model.boundary.output;

import lombok.Builder;

@Builder
public record CategoryReadResponseModel(
        Integer id,
        String name
) {
}
