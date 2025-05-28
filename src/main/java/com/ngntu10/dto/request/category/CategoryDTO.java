package com.ngntu10.dto.request.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotBlank(message = "Category name is required")
    @Schema(
            name = "name",
            description = "Category name",
            type = "String",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Cửa tự động"
    )
    private String name;

    @Schema(
            name = "description",
            description = "Category description",
            type = "String",
            example = "Danh mục các loại cửa tự động"
    )
    private String description;
} 